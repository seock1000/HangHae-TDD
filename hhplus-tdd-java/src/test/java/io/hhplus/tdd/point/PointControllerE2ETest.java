package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointControllerE2ETest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserPointTable userPointTable;
    @Autowired
    private PointHistoryTable pointHistoryTable;

    /**
     * Patch 메소드 미인식 처리 목적
     */
    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }


    /**
     * 검증 로직 테스트는 단위테스트에서 모두 수행하여 e2e 테스트에서 간단한 테스트만 작성하였습니다.
     * db 객체를 변경하지 못하는 경우에 rollback 처리가 어려워 모든 테스트를 한 번에 실행할 때에 에러가 발생합니다.
     * 현재와 같이 DB를 대체하는 Map을 사용할때 rollback 처리를 어떻게 해야할지 궁금합니다.
     */

    @Test
    @DisplayName("포인트 조회시 저장된 유저의 포인트를 조회해야 한다.")
    void point() {
        //given
        long userId = 1L;
        long amount = 100L;
        UserPoint expectedUserPoint = userPointTable.insertOrUpdate(userId, amount);

        String url = "http://localhost:" + port + "/point/" + userId;

        //when
        ResponseEntity<UserPoint> actualUserPoint = testRestTemplate.getForEntity(url, UserPoint.class);

        //then
        assertThat(actualUserPoint.getBody()).isEqualTo(expectedUserPoint);
    }

    @Test
    @DisplayName("포인트 내역 조회시 대상 유저만의 포인트 충전/사옹내역을 조회해야 한다.")
    void history() {
        //given
        long givenUserId = 1L;
        List<PointHistory> expectedPointHistory = new ArrayList<>();
        expectedPointHistory.add(pointHistoryTable.insert(givenUserId, 100L, TransactionType.CHARGE, System.currentTimeMillis()));
        pointHistoryTable.insert(2L, 100L, TransactionType.CHARGE, System.currentTimeMillis());
        expectedPointHistory.add(pointHistoryTable.insert(givenUserId, 100L, TransactionType.USE, System.currentTimeMillis()));

        String url = "http://localhost:" + port + "/point/" + givenUserId + "/histories";

        //when
        ParameterizedTypeReference<List<PointHistory>> pointHistories = new ParameterizedTypeReference<List<PointHistory>>() {};
        ResponseEntity<List<PointHistory>> actualPointHistories = testRestTemplate.exchange(url, HttpMethod.GET, null, pointHistories);

        //then
        assertIterableEquals(actualPointHistories.getBody(), expectedPointHistory);
    }

    @Test
    @DisplayName("포인트 충전시 충전된 유저 포인트 결과를 반환한다.")
    void charge() {
        //given
        UserPoint givenUserPoint = userPointTable.insertOrUpdate(1L, 100L);
        long chargePoint = 100L;
        long expectedPoint = givenUserPoint.point() + chargePoint;
        String url = "http://localhost:" + port + "/point/" + givenUserPoint.id() + "/charge";

        //when
        UserPoint actualUserPoint = testRestTemplate.patchForObject(url, chargePoint, UserPoint.class);

        //then
        assertThat(givenUserPoint.id()).isEqualTo(actualUserPoint.id());
        assertThat(expectedPoint).isEqualTo(actualUserPoint.point());
    }

    @Test
    @DisplayName("포인트 사용시 사용된 유저 포인트 결과를 반환한다.")
    void use() {
        UserPoint givenUserPoint = userPointTable.insertOrUpdate(1L, 200L);
        long usePoint = 100L;
        long expectedPoint = givenUserPoint.point() - usePoint;
        String url = "http://localhost:" + port + "/point/" + givenUserPoint.id() + "/use";

        //when
        UserPoint actualUserPoint = testRestTemplate.patchForObject(url, usePoint, UserPoint.class);

        //then
        assertThat(givenUserPoint.id()).isEqualTo(actualUserPoint.id());
        assertThat(expectedPoint).isEqualTo(actualUserPoint.point());
    }
}