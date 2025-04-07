package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.error.ExceedMaxBalanceError;
import kr.hhplus.be.server.domain.point.error.InsufficientBalanceError;
import kr.hhplus.be.server.domain.point.error.InvalidChargeAmountError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    @DisplayName("포인트 충전 시, 충전금액이 1_000_000이하이며, 잔고가 5_000_000을 초과하지 않으면 충전에 성공한다.")
    void charge() {
        // given
        Point point = new Point(1L, 1L, 4_000_000);

        // when
        point.charge(1_000_000);

        // then
        assertEquals(5_000_000, point.getBalance());
    }

    @Test
    @DisplayName("포인트 충전 시, 충전금액이 1_000_000을 초과하면 InvalidChargeAmountException이 발생한다.")
    void charge_ExceedMaxChargeAmount() {
        // given
        Point point = new Point(1L, 1L, 0);

        // when
        Exception exception = assertThrows(InvalidChargeAmountError.class, () -> point.charge(1_000_001));

        // then
        assertEquals("1회 최대 충전 금액은 1,000,000원입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 충전 시, 잔고가 5_000_000을 초과하면 ExceedMaxBalanceException이 발생한다.")
    void charge_ExceedMaxBalance() {
        // given
        Point point = new Point(1L, 1L, 5_000_000);

        // when
        Exception exception = assertThrows(ExceedMaxBalanceError.class, () -> point.charge(1));

        // then
        assertEquals("보유 잔고는 5,000,000원을 초과할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("포인트 사용 시, 잔고가 사용 금액보다 많거나 같으면 사용에 성공한다.")
    void use() {
        // given
        Point point = new Point(1L, 1L, 1_000_000);

        // when
        point.use(1_000_000);

        // then
        assertEquals(0, point.getBalance());
    }

    @Test
    @DisplayName("포인트 사용 시, 잔고가 부족하면 InsufficientBalanceException이 발생한다.")
    void use_InsufficientBalance() {
        // given
        Point point = new Point(1L, 1L, 0);

        // when
        Exception exception = assertThrows(InsufficientBalanceError.class, () -> point.use(1));

        // then
        assertEquals("잔고가 부족합니다.", exception.getMessage());
    }
}