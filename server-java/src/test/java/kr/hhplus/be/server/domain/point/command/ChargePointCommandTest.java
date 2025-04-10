package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.error.InvalidAmountError;
import kr.hhplus.be.server.domain.point.error.InvalidUserIdError;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargePointCommandTest {

    @Test
    @DisplayName("ChargePointCommand 생성 시, userId와 amount가 0보다 크면 성공한다.")
    void testChargePointCommand_Success() {
        // given
        long userId = 1L;
        int amount = 100;

        // when
        ChargePointCommand command = new ChargePointCommand(userId, amount);

        // then
        assertNotNull(command);
        assertEquals(userId, command.userId());
        assertEquals(amount, command.amount());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시, userId가 0보다 작거나 같으면 InvalidUserIdError 발생한다.")
    void testChargePointCommand_PointNotExistError() {
        // given
        long userId = 0L;
        int amount = 100;

        // when & then
        Exception exception = assertThrows(InvalidUserIdError.class, () -> {
            new ChargePointCommand(userId, amount);
        });
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시, amount가 0보다 작거나 같으면 InvalidAmountError가 발생한다.")
    void testChargePointCommand_InvalidAmountError() {
        // given
        long userId = 1L;
        int amount = -100;

        // when & then
        Exception exception = assertThrows(InvalidAmountError.class, () -> {
            new ChargePointCommand(userId, amount);
        });
        assertEquals("잘못된 충전 금액 형식입니다.", exception.getMessage());
    }

}