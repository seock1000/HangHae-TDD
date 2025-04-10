package kr.hhplus.be.server.domain.user;

public class UserNotExistError extends RuntimeException {
    private UserNotExistError(String message) {
        super(message);
    }

    public static UserNotExistError of(String message) {
        return new UserNotExistError(message);
    }
}
