package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    @DisplayName("포인트 ID와 충전금액, 현재잔고를 입력받아 포인트 충전 이력을 생성한다.")
    void createChargeHistory() {
        // given
        Point point = new Point(1L, 1L, 1_000_000);
        int givenChargeAmount = 1_000_000;

        Long expectedPointId = point.getId();
        int expectedChargeAmount = 1_000_000;
        int expectedCurrentBalance = point.getBalance();
        TransactionType expectedTransactionType = TransactionType.CHARGE;

        // when
        PointHistory pointHistory = PointHistory.createChargeHistory(point, givenChargeAmount);

        // then
        assertEquals(expectedPointId, pointHistory.getPoint());
        assertEquals(expectedChargeAmount, pointHistory.getAmount());
        assertEquals(expectedCurrentBalance, pointHistory.getBalance());
        assertEquals(expectedTransactionType, pointHistory.getType());
    }

    @Test
    @DisplayName("포인트 ID와 사용금액, 현재잔고를 입력받아 포인트 사용 이력을 생성한다.")
    void createUseHistory() {
        // given
        Point point = new Point(1L, 1L, 1_000_000);
        int givenUseAmount = 1_000_000;

        Long expectedPointId = point.getId();
        int expectedUseAmount = 1_000_000;
        int expectedCurrentBalance = point.getBalance();
        TransactionType expectedTransactionType = TransactionType.USE;

        // when
        PointHistory pointHistory = PointHistory.createUseHistory(point, givenUseAmount);

        // then
        assertEquals(expectedPointId, pointHistory.getPoint());
        assertEquals(expectedUseAmount, pointHistory.getAmount());
        assertEquals(expectedCurrentBalance, pointHistory.getBalance());
        assertEquals(expectedTransactionType, pointHistory.getType());
    }
}