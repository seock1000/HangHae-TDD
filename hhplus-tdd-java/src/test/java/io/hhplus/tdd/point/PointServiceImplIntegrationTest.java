package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointConstant;
import io.hhplus.tdd.point.dto.ChargePointDto;
import io.hhplus.tdd.point.dto.UsePointDto;
import io.hhplus.tdd.point.util.UserPointLockManager;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

public class PointServiceImplIntegrationTest {

    private UserPointTable userPointTable;
    private PointHistoryTable pointHistoryTable;
    private PointServiceImpl pointService;

    /**
     * @Transaction 사용이 불가능하고, Table class에 delete 메서드도 존재하지 않아 모든 테스트 실행시 잔존 데이터로 인해 테스트 실패
     * 해결을 위해 beforeEach로 DI 직접 수행
     */
    @BeforeEach
    void setDependencies() {
        userPointTable = new UserPointTable();
        pointHistoryTable = new PointHistoryTable();
        pointService = new PointServiceImpl(userPointTable, pointHistoryTable, new UserPointLockManager());
    }
    /**
     * 1. 포인트 조회
     * - 유저가 등록되지 않은 경우 => 해당 유저의 포인트는 0으로 조회
     * - 유저가 등록된 경우 => 해당 유저의 등록된 포인트를 조회
     * 2. 포인트 내역 조회
     * - 해당 유저의 내역만 조회
     * - 저장된 순서대로 조회
     * 3. 포인트 충전
     * - 유저 ID가 0 이하인 경우 => IllegalArgumentException (dto 검증)
     * - 충전액이 0 이하인 경우 => IllegalArgumentException (dto 검증)
     * - 충전액 + 보유액이 최대 잔고 이상인 경우 => ExceedMaxPointException (domain 검증)
     * - 포인트 충전 성공 => 보유 포인트를 업데이트하고 포인트 내역을 충전한 것으로 저장한다 // 현재 보유 포인트를 반환하는지는 단위테스트에서 검증했기 때문에 통합테스트의 관심사가 아니라고 판단
     * 3. 포인트 사용
     * - 유저 ID가 0 이하인 경우 => IllegalArgumentException (dto 검증)
     * - 사용액이 0 이하인 경우 => IllegalArgumentException (dto 검증)
     * - 보유액 - 사용액이 최소 잔고 이상인 경우 => UnderMinPointException (domain 검증)
     * - 포인트 사용 성공 => 보유 포인트를 업데이트하고 포인트 내역을 사용한 것으로 저장한다 // 현재 보유 포인트를 반환하는지는 단위테스트에서 검증했기 때문에 통합테스트의 관심사가 아니라고 판단
     */

    @Test
    @DisplayName("포인트 조회시 등록되지 않은 유저의 경우 포인트는 0으로 조회된다")
    void readUnregisterUserPoint() {
        //given
        long userId = 1L;

        //when
        UserPoint userPoint = pointService.readUserPoint(userId);

        //then
        assertThat(userPoint.id()).isEqualTo(userId);
        assertThat(userPoint.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("포인트 조회시 등록된 유저의 경우 등록된 포인트를 조회한다.")
    void readUserPoint() {
        //given
        long givenUserId = 1L;
        long givenPoint = 100L;
        UserPoint initUserPoint = userPointTable.insertOrUpdate(givenUserId, givenPoint);

        //when
        UserPoint readUserPoint = pointService.readUserPoint(givenUserId);

        //then
        assertThat(initUserPoint).isEqualTo(readUserPoint);
    }

    @Test
    @DisplayName("포인트 내역 조회시 포인트 내역은 해당하는 유저의 포인트 내역만을 저장된 순서대로 조회된다.")
    void readUserPointHistoryAsSeq() {
        //given
        long givenUserId = 1L;
        long notGivenUserId = 2L;
        List<PointHistory> expectedPointHistories = new ArrayList<>();
        expectedPointHistories.add(pointHistoryTable.insert(givenUserId, 100L, TransactionType.CHARGE, System.currentTimeMillis()));
        pointHistoryTable.insert(notGivenUserId, 100L, TransactionType.CHARGE, System.currentTimeMillis());
        expectedPointHistories.add(pointHistoryTable.insert(givenUserId, 100L, TransactionType.USE, System.currentTimeMillis()));

        //when
        List<PointHistory> actualHistories = pointService.readPointHistoryByUserId(givenUserId);

        //then
        Assertions.assertIterableEquals(expectedPointHistories, actualHistories);
    }

    @Test
    @DisplayName("포인트 충전 성공시 보유 포인트를 업데이트하고 포인트 내역을 충전으로 저장하며 현재 포인트를 반환한다")
    void successChargePoint() {
        //given
        ChargePointDto givenDto = new ChargePointDto(1L, 100L);
        long expectedPoint = 100L;

        //when
        pointService.chargeUserPoint(givenDto);

        //then
        UserPoint updatedUserPoint = userPointTable.selectById(givenDto.userId());
        assertThat(updatedUserPoint.point()).isEqualTo(expectedPoint);

        PointHistory pointHistory = pointHistoryTable.selectAllByUserId(givenDto.userId()).get(0);
        assertThat(pointHistory.amount()).isEqualTo(expectedPoint);
        assertThat(pointHistory.type()).isEqualTo(TransactionType.CHARGE);
        assertThat(pointHistory.updateMillis()).isEqualTo(updatedUserPoint.updateMillis());
    }

    /**
     * 동시성 테스트
     * - 같은 사용자의 충전 요청이 동시에 여러번 이루어질 때, 모든 요청이 정상적으로 반영되어야 한다.
     * - 같은 사용자의 사용 요청이 동시에 여러번 이루어질 때, 모든 요청이 정상적으로 반영되어야 한다.
     */

    @Test
    @DisplayName("포인트 충전시, 같은 사용자의 동시 요청 여러번이 모두 정상 반영되어야 한다.")
    void concurrentChargeTest() throws InterruptedException {
        //given
        int latchSize = Long.valueOf(5L).intValue();
        CountDownLatch latch = new CountDownLatch(latchSize);

        //when
        for(int i = 0; i < latchSize; i++) {
            new Thread(() -> {
                pointService.chargeUserPoint(new ChargePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        latch.await();

        //then
        UserPoint resultUserPoint = userPointTable.selectById(1L);
        assertThat(resultUserPoint.point()).isEqualTo(5L);
    }

    @Test
    @DisplayName("포인트 사용시, 같은 사용자의 동시 요청 여러번이 모두 정상 반영되어야 한다.")
    void concurrentUseTest() throws InterruptedException {
        //given
        userPointTable.insertOrUpdate(1L, 10L);
        int latchSize = Long.valueOf(5L).intValue();
        CountDownLatch latch = new CountDownLatch(latchSize);

        //when
        for(int i = 0; i < latchSize; i++) {
            new Thread(() -> {
                pointService.useUserPoint(new UsePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        latch.await();

        //then
        UserPoint resultUserPoint = userPointTable.selectById(1L);
        assertThat(resultUserPoint.point()).isEqualTo(5L);
    }

    /**
     * 피드백 반영 테스트 추가
     * 1. 같은 사용자의 사용/충전 요청 테스트
     * 2. 서로 다른 사용자의 동시 요청 테스트
     */

    @Test
    @DisplayName("같은 사용자의 포인트 사용/충전 요청이 모두 정상반영 되어야 한다.")
    void concurrentUseAndChargeTest() throws InterruptedException {
        //given
        userPointTable.insertOrUpdate(1L, 10L);
        int useNum = 3;
        int chargeNum = 2;
        int latchSize = useNum + chargeNum;
        CountDownLatch latch = new CountDownLatch(latchSize);

        long expected = 9L;

        //when
        for(int i = 0; i < useNum; i++) {
            new Thread(() -> {
                pointService.useUserPoint(new UsePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        for(int i = 0; i < chargeNum; i++) {
            new Thread(() -> {
                pointService.chargeUserPoint(new ChargePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        latch.await();

        //then
        UserPoint resultUserPoint = userPointTable.selectById(1L);
        assertThat(resultUserPoint.point()).isEqualTo(expected);
    }

    @Test
    @DisplayName("다른 사용자의 포인트 사용/충전 요청이 모두 정상반영 되어야 한다.")
    void concurrentUsersUseAndChargeTest() throws InterruptedException {
        //given
        userPointTable.insertOrUpdate(1L, 10L);
        userPointTable.insertOrUpdate(2L, 10L);
        int useNum = 3;
        int chargeNum = 2;
        int latchSize = (useNum + chargeNum) * 2;
        CountDownLatch latch = new CountDownLatch(latchSize);

        long expected = 9L;

        //when
        for(int i = 0; i < useNum; i++) {
            new Thread(() -> {
                pointService.useUserPoint(new UsePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        for(int i = 0; i < useNum; i++) {
            new Thread(() -> {
                pointService.useUserPoint(new UsePointDto(2L, 1L));
                latch.countDown();
            }).start();
        }
        for(int i = 0; i < chargeNum; i++) {
            new Thread(() -> {
                pointService.chargeUserPoint(new ChargePointDto(1L, 1L));
                latch.countDown();
            }).start();
        }
        for(int i = 0; i < chargeNum; i++) {
            new Thread(() -> {
                pointService.chargeUserPoint(new ChargePointDto(2L, 1L));
                latch.countDown();
            }).start();
        }
        latch.await();

        //then
        UserPoint resultUserPoint1 = userPointTable.selectById(1L);
        UserPoint resultUserPoint2 = userPointTable.selectById(2L);
        assertThat(resultUserPoint1.point()).isEqualTo(expected);
        assertThat(resultUserPoint2.point()).isEqualTo(expected);
    }
}
