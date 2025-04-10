package kr.hhplus.be.server.domain.user;

public class InvalidUserIdError extends RuntimeException {
    private InvalidUserIdError(String message) {
        super(message);
    }

    public static InvalidUserIdError of(String message) {
        return new InvalidUserIdError(message);
    }
}
