package kr.hhplus.be.server.application.point.error;

public class PointInfoNotFoundError extends RuntimeException {
    public static PointInfoNotFoundError of(String message) {
        return new PointInfoNotFoundError(message);
    }

    private PointInfoNotFoundError(String message) {
        super(message);
    }
}
