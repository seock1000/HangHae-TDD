package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PointRepositoryImpl implements PointRepository {

    @Override
    public Point save(Point point) {
        return null;
    }

    @Override
    public Optional<Point> findByUserId(long userId) {
        return Optional.empty();
    }

    @Override
    public PointHistory saveHistory(PointHistory pointHistory) {
        return null;
    }
}
