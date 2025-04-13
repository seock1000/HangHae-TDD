package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;
    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("null 허용 포인트 조회 시, 포인트가 존재하면 포인트 정보를 반환한다.")
    void getPointByUserIdWithEmpty() {
        // given
        long userId = 1L;
        Point point = Instancio.of(Point.class)
                .set(field("user"), userId)
                .create();
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(point));

        // when
        Optional<Point> result = pointService.getPointByUserIdWithEmpty(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(point, result.get());
    }

    @Test
    @DisplayName("null 허용 포인트 조회 시, 포인트가 존재하지 않으면 Optional.empty()를 반환한다.")
    void getPointByUserIdWithEmpty_NoPoint() {
        // given
        long userId = 1L;
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when
        Optional<Point> result = pointService.getPointByUserIdWithEmpty(userId);

        // then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("포인트 조회 시, 포인트가 존재하면 포인트 정보를 반환한다.")
    void getPointByUserId() {
        // given
        long userId = 1L;
        Point point = Instancio.of(Point.class)
                .set(field("user"), userId)
                .create();
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(point));

        // when
        Point result = pointService.getPointByUserId(userId);

        // then
        assertEquals(point, result);
    }

    @Test
    @DisplayName("포인트 조회 시, 포인트가 존재하지 않으면 ApiException(POINT_NOT_FOUND)을 발생시킨다.")
    void getPointByUserId_NoPoint() {
        // given
        long userId = 1L;
        when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> pointService.getPointByUserId(userId));

        // then
        assertEquals(ApiError.POINT_NOT_FOUND, exception.getApiError());
    }
/*
    @Test
    @DisplayName("포인트 충전 시, 포인트 잔액을 증가시키고 업데이트된 포인트와 충전 내역을 저장한다.")
    void charge() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 1000)
                .create();
        int chargeAmount = 500;
        when(pointRepository.save(point)).thenReturn(point);

        // when
        Point result = pointService.charge(point, chargeAmount);

        // then
        verify(pointRepository).save(any());
        verify(pointRepository).saveHistory(any());
        assertEquals(1500, result.getBalance());
    }

    @Test
    @DisplayName("포인트 사용 시, 포인트 잔액을 감소시키고 업데이트된 포인트와 사용 내역을 저장한다.")
    void use() {
        // given
        Point point = Instancio.of(Point.class)
                .set(field("balance"), 1000)
                .create();
        int useAmount = 500;
        when(pointRepository.save(point)).thenReturn(point);

        // when
        Point result = pointService.use(point, useAmount);

        // then
        verify(pointRepository).save(any());
        verify(pointRepository).saveHistory(any());
        assertEquals(500, result.getBalance());
    }

 */

}