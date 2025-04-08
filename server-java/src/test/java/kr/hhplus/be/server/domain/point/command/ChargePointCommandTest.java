package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.error.InvalidAmountError;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChargePointCommandTest {

    @Test
    @DisplayName("ChargePointCommand 생성 시, point가 null이 아니고 amount가 0보다 크면 성공한다.")
    void testChargePointCommand_Success() {
        // given
        Point point = new Point(1L, 1L, 1000);
        int amount = 100;

        // when
        ChargePointCommand command = new ChargePointCommand(point, amount);

        // then
        assertNotNull(command);
        assertEquals(point, command.point());
        assertEquals(amount, command.amount());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시, point가 null이면 PointNotExistError가 발생한다.")
    void testChargePointCommand_PointNotExistError() {
        // given
        Point point = null;
        int amount = 100;

        // when & then
        Exception exception = assertThrows(PointNotExistError.class, () -> {
            new ChargePointCommand(point, amount);
        });
        assertEquals("포인트 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("ChargePointCommand 생성 시, amount가 0보다 작거나 같으면 InvalidAmountError가 발생한다.")
    void testChargePointCommand_InvalidAmountError() {
        // given
        Point point = new Point(1L, 1L, 1000);
        int amount = -100;

        // when & then
        Exception exception = assertThrows(InvalidAmountError.class, () -> {
            new ChargePointCommand(point, amount);
        });
        assertEquals("잘못된 충전 금액 형식입니다.", exception.getMessage());
    }

}