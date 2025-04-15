package kr.hhplus.be.server.domain.point;

public class UsedPoint {
    private Point point;

    private UsedPoint(Point point) {
        this.point = point;
    }

    public static UsedPoint of(Point point) {
        return new UsedPoint(point);
    }

    public void use(int amount) {
        point.use(amount);
    }
}
