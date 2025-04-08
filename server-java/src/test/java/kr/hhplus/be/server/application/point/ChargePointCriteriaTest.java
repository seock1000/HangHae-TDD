package kr.hhplus.be.server.application.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargePointCriteriaTest {

    @Test
    @DisplayName("형식적으로 유효한 사용자 ID와 충전 금액이 주어지면 ChargePointCriteria 객체를 생성한다.")
    void testValidChargePointCriteria() {
        // given
        long userId = 1L;
        int amount = 1000;

        // when
        ChargePointCriteria criteria = new ChargePointCriteria(userId, amount);

        // then
        assertEquals(userId, criteria.userId());
        assertEquals(amount, criteria.amount());
    }

    @Test
    @DisplayName("사용자 ID가 0보다 작거나 같으면 IllegalArgumentException이 발생한다.")
    void testInvalidUserId() {
        // given
        long userId = 0L;
        int amount = 1000;

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChargePointCriteria(userId, amount);
        });
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("충전 금액이 0보다 작거나 같으면 IllegalArgumentException이 발생한다.")
    void testInvalidAmount() {
        // given
        long userId = 1L;
        int amount = 0;

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChargePointCriteria(userId, amount);
        });
        assertEquals("잘못된 충전 금액 형식입니다.", exception.getMessage());
    }

}