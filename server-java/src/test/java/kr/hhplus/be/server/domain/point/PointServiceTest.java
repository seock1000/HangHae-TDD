package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.command.ChargePointCommand;
import kr.hhplus.be.server.domain.point.command.UsePointCommand;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("포인트 충전 시, 포인트를 충전하고 포인트와 충전이력을 저장한다.")
    void testChargePoint() {
        // given
        long givenPointId = 1L;
        long givenUserId = 1L;
        int givenBalance = 1_000_000;
        int chargeAmount = 1_000_000;
        Point givenPoint = new Point(givenPointId, givenUserId, givenBalance);

        ChargePointCommand command = new ChargePointCommand(givenUserId, chargeAmount);

        Point expectedPoint = new Point(givenPointId, givenUserId, givenBalance + chargeAmount);
        PointHistory expectedPointHistory = PointHistory.createChargeHistory(expectedPoint, chargeAmount);

        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.of(givenPoint));
        when(pointRepository.save(any())).thenReturn(expectedPoint);
        when(pointHistoryRepository.save(any())).thenReturn(expectedPointHistory);

        // when
        pointService.charge(command);

        // then
        verify(pointRepository).findByUserId(givenUserId);
        verify(pointRepository).save(expectedPoint);
        verify(pointHistoryRepository).save(expectedPointHistory);
    }

    @Test
    @DisplayName("포인트 충전 시, 포인트 정보가 존재하지 않으면 PointNotExistError를 발생시킨다.")
    void testChargePoint_PointNotExistError() {
        // given
        long givenUserId = 1L;
        int chargeAmount = 1_000_000;
        ChargePointCommand command = new ChargePointCommand(givenUserId, chargeAmount);

        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PointNotExistError.class, () -> {
            pointService.charge(command);
        });
    }

    @Test
    @DisplayName("포인트 사용 시, 포인트를 사용하고 포인트와 사용이력을 저장한다.")
    void testUsePoint() {
        // given
        long givenPointId = 1L;
        long givenUserId = 1L;
        int givenBalance = 1_000_000;
        int useAmount = 1_000_000;
        Point givenPoint = new Point(givenPointId, givenUserId, givenBalance);

        UsePointCommand command = new UsePointCommand(givenUserId, useAmount);

        Point expectedPoint = new Point(givenPointId, givenUserId, givenBalance - useAmount);
        PointHistory expectedPointHistory = PointHistory.createUseHistory(expectedPoint, useAmount);

        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.of(givenPoint));
        when(pointRepository.save(any())).thenReturn(expectedPoint);
        when(pointHistoryRepository.save(any())).thenReturn(expectedPointHistory);

        // when
        pointService.use(command);

        // then
        verify(pointRepository).findByUserId(givenUserId);
        verify(pointRepository).save(expectedPoint);
        verify(pointHistoryRepository).save(expectedPointHistory);
    }

    @Test
    @DisplayName("포인트 사용 시, 포인트 정보가 존재하지 않으면 PointNotExistError를 발생시킨다.")
    void testUsePoint_PointNotExistError() {
        // given
        long givenUserId = 1L;
        int useAmount = 1_000_000;
        UsePointCommand command = new UsePointCommand(givenUserId, useAmount);

        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PointNotExistError.class, () -> {
            pointService.use(command);
        });
    }

}