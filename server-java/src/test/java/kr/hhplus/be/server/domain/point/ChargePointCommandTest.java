package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargePointCommandTest {

    @Test
    @DisplayName("ChargePointCommand 생성 시 0보다 큰 사용자 ID와 충전금액을 받으면 생성에 성공한다.")
    void ChargePointCommand_ValidArgs_Success() {
        // given
        Long userId = 1L;
        Integer amount = 1;

        // when
        ChargePointCommand command = new ChargePointCommand(userId, amount);

        // then
        assertEquals(userId, command.userId());
        assertEquals(amount, command.amount());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시 userId가 0이하이면 IllegalArgumentException 예외가 발생한다.")
    void ChargePointCommand_NegativeUserId_ThrowsIllegalArgumentException() {
        // given
        Long userId = 0L;
        Integer amount = 1;

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChargePointCommand(userId, amount);
        });
        assertEquals("유효하지 않은 사용자 ID 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시 amount가 0이하이면 IllegalArgumentException 예외가 발생한다.")
    void ChargePointCommand_NegativeAmount_ThrowsIllegalArgumentException() {
        // given
        Long userId = 1L;
        Integer amount = 0;

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ChargePointCommand(userId, amount);
        });
        assertEquals("유효하지 않은 충전 금액입니다.", exception.getMessage());
    }
}