package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    @DisplayName("포인트 ID와 충전금액, 현재잔고를 입력받아 포인트 충전 이력을 생성한다.")
    void createChargeHistory() {
        // given
        Long pointId = 1L;
        int chargeAmount = 1_000_000;
        int currentBalance = 1_000_000;

        // when
        PointHistory pointHistory = PointHistory.createChargeHistory(pointId, chargeAmount, currentBalance);

        // then
        assertEquals(pointId, pointHistory.getPoint());
        assertEquals(chargeAmount, pointHistory.getAmount());
        assertEquals(currentBalance, pointHistory.getBalance());
        assertEquals(TransactionType.CHARGE, pointHistory.getType());
    }

    @Test
    @DisplayName("포인트 ID와 사용금액, 현재잔고를 입력받아 포인트 사용 이력을 생성한다.")
    void createUseHistory() {
        // given
        Long pointId = 1L;
        int useAmount = 500_000;
        int currentBalance = 500_000;

        // when
        PointHistory pointHistory = PointHistory.createUseHistory(pointId, useAmount, currentBalance);

        // then
        assertEquals(pointId, pointHistory.getPoint());
        assertEquals(useAmount, pointHistory.getAmount());
        assertEquals(currentBalance, pointHistory.getBalance());
        assertEquals(TransactionType.USE, pointHistory.getType());
    }
}