package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("포인트 충전 시, 포인트 잔액이 증가한다.")
    void charge() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 1000)
                .create();

        // when
        point.charge(500);

        // then
        assertEquals(1500, point.getBalance());
    }

    @Test
    @DisplayName("포인트 충전 시 포인트 충전 금액이 1_000_000원을 초과할 경우 ApiException(EXCEED_CHARGE_LIMIT)을 발생시킨다.")
    void charge_ExceedChargeLimit() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 0)
                .create();

        // when
        ApiException exception = assertThrows(ApiException.class, () -> point.charge(1_000_001));

        // then
        assertEquals(ApiError.EXCEED_CHARGE_LIMIT, exception.getApiError());
    }

    @Test
    @DisplayName("포인트 충전 시 충전 후 포인트 잔액이 5_000_000원을 초과할 경우 ApiException(EXCEED_BALANCE_LIMIT)을 발생시킨다.")
    void charge_ExceedBalanceLimit() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 5_000_000)
                .create();

        // when
        ApiException exception = assertThrows(ApiException.class, () -> point.charge(1));

        // then
        assertEquals(ApiError.EXCEED_BALANCE_LIMIT, exception.getApiError());
    }

    @Test
    @DisplayName("포인트 사용 시, 포인트 잔액이 감소한다.")
    void use() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 1000)
                .create();

        // when
        point.use(500);

        // then
        assertEquals(500, point.getBalance());
    }

    @Test
    @DisplayName("포인트 사용 시 포인트 사용 후 금액이 0원 미만일 경우 ApiException(UNDER_BALANCE_LIMIT)을 발생시킨다.")
    void use_UnderBalanceLimit() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 0)
                .create();

        // when
        ApiException exception = assertThrows(ApiException.class, () -> point.use(1));

        // then
        assertEquals(ApiError.UNDER_BALANCE_LIMIT, exception.getApiError());
    }
}