package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.error.InvalidAmountError;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateChargeHistoryCommandTest {

    @Test
    @DisplayName("CreateChargeHistoryCommand 생성 시, 포인트 객체와 현재잔고를 입력받아 포인트 충전 이력을 생성한다.")
    void testCreateChargeHistoryCommand_Success() {
        // given
        Point point = new Point(1L, 1L, 1000);
        int chargeAmount = 1000;

        // when
        CreateChargeHistoryCommand command = new CreateChargeHistoryCommand(point, chargeAmount);

        // then
        assertNotNull(command);
        assertEquals(point, command.point());
        assertEquals(chargeAmount, command.amount());
    }

    @Test
    @DisplayName("CreateChargeHistoryCommand 생성 시, 포인트 객체가 null이면 PointNotExistError가 발생한다.")
    void testCreateChargeHistoryCommand_PointNotExistError() {
        // given
        Point point = null;
        int chargeAmount = 1000;

        // when & then
        Exception exception = assertThrows(PointNotExistError.class, () -> {
            new CreateChargeHistoryCommand(point, chargeAmount);
        });
        assertEquals("포인트 정보가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("CreateChargeHistoryCommand 생성 시, 충전 금액이 0보다 작거나 같으면 InvalidAmountError가 발생한다.")
    void testCreateChargeHistoryCommand_InvalidAmountError() {
        // given
        Point point = new Point(1L, 1L, 1000);
        int chargeAmount = 0;

        // when & then
        Exception exception = assertThrows(InvalidAmountError.class, () -> {
            new CreateChargeHistoryCommand(point, chargeAmount);
        });
        assertEquals("잘못된 충전 금액 형식입니다.", exception.getMessage());
    }

}