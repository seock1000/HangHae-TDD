package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.PointConstant;
import io.hhplus.tdd.point.domain.error.ExceedMaxPointError;
import io.hhplus.tdd.point.domain.error.UnderMinPointError;
import io.hhplus.tdd.point.dto.ChargePointDto;
import io.hhplus.tdd.point.dto.UsePointDto;
import io.hhplus.tdd.point.util.UserPointLockManager;
import jakarta.annotation.PostConstruct;
import org.apache.catalina.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.NumberFormat;

import java.io.PipedInputStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceImplTest {

    @Mock
    private UserPointTable userPointTable;
    @Mock
    private PointHistoryTable pointHistoryTable;
    @Mock
    private UserPointLockManager userPointLockManager;
    @InjectMocks
    private PointServiceImpl pointService;


    /**
     * PointService 유저 포인트 충전 테스트 - 성공
     * 형식적인 값의 검증 및 정책적인 값의 검증 테스트는 dto, domain 테스트에서 진행하였으므로, 최소한의 테스트 수행
     */
    @Test
    @DisplayName("포인트 충전에 성공하면 충전 결과 포인트를 반환한다.")
    void chargeUserPoint() {
        //given
        UserPoint initUserPoint = new UserPoint(1L, 100L, System.currentTimeMillis());
        ChargePointDto chargePoint = new ChargePointDto(initUserPoint.id(), 100L);
        UserPoint updatedUserPoint = new UserPoint(1L , initUserPoint.point() + chargePoint.amount(), System.currentTimeMillis());

        when(userPointTable.selectById(anyLong())).thenReturn(initUserPoint);
        when(userPointTable.insertOrUpdate(anyLong(), anyLong())).thenReturn(updatedUserPoint);

        //when
        UserPoint actualUserPoint = pointService.chargeUserPoint(chargePoint);

        //then
        assertThat(actualUserPoint.id()).isEqualTo(updatedUserPoint.id());
        assertThat(actualUserPoint.point()).isEqualTo(updatedUserPoint.point());
        //mock - pointHistoryTable이 호출 되었는가
        verify(pointHistoryTable).insert(eq(updatedUserPoint.id()), eq(updatedUserPoint.point()), eq(TransactionType.CHARGE), eq(updatedUserPoint.updateMillis()));
    }

    /**
     * PointService 유저 포인트 사용 테스트 - 성공
     * 형식적인 값의 검증 및 정책적인 값의 검증 테스트는 dto, domain 테스트에서 진행하였으므로, 최소한의 테스트 수행
     */
    @Test
    @DisplayName("포인트 사용에 성공하면 사용 결과 포인트를 반환한다.")
    void useUserPoint() {
        //given
        UserPoint initUserPoint = new UserPoint(1L, 200L, System.currentTimeMillis());
        UsePointDto usePoint = new UsePointDto(initUserPoint.id(), 100L);
        UserPoint updatedUserPoint = new UserPoint(1L , initUserPoint.point() - usePoint.amount(), System.currentTimeMillis());

        when(userPointTable.selectById(anyLong())).thenReturn(initUserPoint);
        when(userPointTable.insertOrUpdate(anyLong(), anyLong())).thenReturn(updatedUserPoint);

        //when
        UserPoint actualUserPoint = pointService.useUserPoint(usePoint);

        //then
        assertThat(actualUserPoint.id()).isEqualTo(updatedUserPoint.id());
        assertThat(actualUserPoint.point()).isEqualTo(updatedUserPoint.point());
        //mock - pointHistoryTable이 호출 되었는가
        verify(pointHistoryTable).insert(eq(updatedUserPoint.id()), eq(updatedUserPoint.point()), eq(TransactionType.USE), eq(updatedUserPoint.updateMillis()));
    }
}