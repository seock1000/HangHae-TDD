package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface PointRepository {

    public Point save(Point point);

    public Optional<Point> findByUserId(long userId);
}
