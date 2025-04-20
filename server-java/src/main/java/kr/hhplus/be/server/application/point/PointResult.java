package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;

public record PointResult(
        long pointId,
        int balance
) {
    public static PointResult of(Point point) {
        return new PointResult(
                point.getId(),
                point.getBalance()
        );
    }
}
