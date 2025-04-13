package kr.hhplus.be.server;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "Not found"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
