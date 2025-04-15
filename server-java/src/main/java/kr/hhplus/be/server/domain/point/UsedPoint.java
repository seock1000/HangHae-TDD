package kr.hhplus.be.server.domain.point;

/**
 * 포인트 사용 목적 객체
 */
public class UsedPoint {
    private Point point;

    private UsedPoint(Point point) {
        this.point = point;
    }

    protected static UsedPoint of(Point point) {
        return new UsedPoint(point);
    }

    public void use(int amount) {
        this.point.use(amount);
    }
}
