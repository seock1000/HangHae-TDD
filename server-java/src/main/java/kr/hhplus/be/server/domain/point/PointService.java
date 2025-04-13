package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Optional<Point> getPointByUserIdWithEmpty(long userId) {
        return pointRepository.findByUserId(userId);
    }

    public Point getPointByUserId(long userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> ApiException.of(ApiError.POINT_NOT_FOUND));
    }

    public Point charge(Point point, int amount) {
        point.charge(amount);
        return point;
    }

    public Point use(Point point, int amount) {
        point.use(amount);
        return point;
    }

    public Point save(Point point) {
        return pointRepository.saveWithHistory(point);
    }
}
