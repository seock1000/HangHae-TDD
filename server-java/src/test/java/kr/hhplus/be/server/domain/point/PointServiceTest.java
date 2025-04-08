package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.command.ChargePointCommand;
import kr.hhplus.be.server.domain.point.command.CreateChargeHistoryCommand;
import kr.hhplus.be.server.domain.point.command.CreateUseHistoryCommand;
import kr.hhplus.be.server.domain.point.command.UsePointCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PointServiceTest {

    PointService pointService = new PointService();

    @Test
    @DisplayName("포인트 충전 시, 충전금액이 1_000_000이하이며, 잔고가 5_000_000을 초과하지 않으면 충전에 성공한다.")
    void charge() {
        // given
        Point given = new Point(1L, 1L, 4_000_000);
        Point expected = new Point(1L, 1L, 5_000_000);
        int chargeAmount = 1_000_000;
        ChargePointCommand command = new ChargePointCommand(given, chargeAmount);

        // when
        pointService.charge(command);

        // then
        assertEquals(expected, given);
    }

    @Test
    @DisplayName("포인트 사용 시, 잔고가 사용 금액보다 많거나 같으면 사용에 성공한다.")
    void use() {
        // given
        Point given = new Point(1L, 1L, 1_000_000);
        Point expected = new Point(1L, 1L, 0);
        int useAmount = 1_000_000;
        UsePointCommand command = new UsePointCommand(given, useAmount);

        // when
        pointService.use(command);

        // then
        assertEquals(expected, given);
    }

    @Test
    @DisplayName("Point 객체와 충전금액을 입력받아 포인트 충전 이력을 생성한다.")
    void createChargeHistory() {
        // given
        Point point = new Point(1L, 1L, 1_000_000);
        int chargeAmount = 1_000_000;
        PointHistory expected = PointHistory.createChargeHistory(point.getId(), chargeAmount, point.getBalance());
        CreateChargeHistoryCommand command = new CreateChargeHistoryCommand(point, chargeAmount);

        // when
        PointHistory actual = pointService.createChargeHistory(command);

        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Point 객체와 사용금액을 입력받아 포인트 사용 이력을 생성한다.")
    void useHistory() {
        // given
        Point point = new Point(1L, 1L, 1_000_000);
        int useAmount = 500_000;
        PointHistory expected = PointHistory.createUseHistory(point.getId(), useAmount, point.getBalance());
        CreateUseHistoryCommand command = new CreateUseHistoryCommand(point, useAmount);

        // when
        PointHistory actual = pointService.useHistory(command);

        // then
        assertEquals(expected, actual);
    }
}