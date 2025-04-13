package kr.hhplus.be.server.domain.point;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    @DisplayName("포인트 충전 내역 생성 시, 포인트 충전 내역이 생성된다.")
    void createChargeHistory() {
        // given
        int chargeAmount = 1000;
        Point point = Instancio.of(Point.class)
                .set(field("id"), 1L)
                .set(field("balance"), 10000)
                .create();

        // when
        PointHistory pointHistory = PointHistory.createChargeHistory(point, 1000);

        // then
        assertNotNull(pointHistory);
        assertEquals(point.getId(), pointHistory.getPoint());
        assertEquals(chargeAmount, pointHistory.getAmount());
        assertEquals(point.getBalance(), pointHistory.getBalance());
        assertEquals(TransactionType.CHARGE, pointHistory.getType());
    }

    @Test
    @DisplayName("포인트 사용 내역 생성 시, 포인트 사용 내역이 생성된다.")
    void createUseHistory() {
        // given
        int useAmount = 500;
        Point point = Instancio.of(Point.class)
                .set(field("id"), 1L)
                .set(field("balance"), 10000)
                .create();

        // when
        PointHistory pointHistory = PointHistory.createUseHistory(point, useAmount);

        // then
        assertNotNull(pointHistory);
        assertEquals(point.getId(), pointHistory.getPoint());
        assertEquals(useAmount, pointHistory.getAmount());
        assertEquals(point.getBalance(), pointHistory.getBalance());
        assertEquals(TransactionType.USE, pointHistory.getType());
    }


}