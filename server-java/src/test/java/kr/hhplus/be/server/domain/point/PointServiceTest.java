package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PointServiceTest {

    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("포인트 조회 시, 유효한 사용자 ID를 받으면 사용자 포인트 잔고를 반환한다.")
    void getPointByUserId() {
        //given
        GetPointCommand command = new GetPointCommand(1L);
        Point expected = new Point(1L, 1L, 10000);
        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.of(expected));

        //when
        Point point = pointService.getPointByUserId(command).get();

        //then
        assertEquals(expected, point);
    }

    @Test
    @DisplayName("포인트 조회 시, 유효한 사용자의 ID로 생성된 포인트 정보가 존재하지 않으면 Optional.empty를 반환한다.")
    void getPointByUserId_NoPointInfo() {
        //given
        GetPointCommand command = new GetPointCommand(1L);

        when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        //when
        Optional<Point> point = pointService.getPointByUserId(command);

        //then
        assertEquals(Optional.empty(), point);
    }
}