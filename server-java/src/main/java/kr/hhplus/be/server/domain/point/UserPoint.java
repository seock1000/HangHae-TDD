package kr.hhplus.be.server.domain.point;

public class UserPoint {
    private Point point;

    private UserPoint(Point point) {
        this.point = point;
    }

    public static UserPoint of(Point point) {
        return new UserPoint(point);
    }

    public void use(int amount) {
        point.use(amount);
    }
}
