package kr.hhplus.be.server.domain.point.error;

public class InvalidUserIdError extends RuntimeException {
    public static InvalidUserIdError of(String message) {
        return new InvalidUserIdError(message);
    }

    private InvalidUserIdError(String message) {
        super(message);
    }
}
