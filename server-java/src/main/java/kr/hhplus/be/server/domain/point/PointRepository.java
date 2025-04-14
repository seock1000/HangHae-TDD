package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface PointRepository {

    public Optional<Point> findByUserId(long userId);

    public Point saveWithHistory(Point point);

}
