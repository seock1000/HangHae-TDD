package kr.hhplus.be.server.domain.point;

/**
 * 포인트 사용 목적 객체
 */
public class PointDeductor {
    private Point point;

    private PointDeductor(Point point) {
        this.point = point;
    }

    protected static PointDeductor of(Point point) {
        return new PointDeductor(point);
    }

    public void use(int amount) {
        this.point.use(amount);
    }
}
