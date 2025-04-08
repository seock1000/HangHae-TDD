package kr.hhplus.be.server.domain.point.error;

public class PointNotExistError extends RuntimeException {
    public static PointNotExistError of(String message) {
        return new PointNotExistError(message);
    }

    private PointNotExistError(String message) {
        super(message);
    }
}
