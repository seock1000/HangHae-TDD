package kr.hhplus.be.server;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {

    //point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "Point not found"),
    EXCEED_CHARGE_LIMIT(HttpStatus.BAD_REQUEST, "Exceed charge limit"),
    EXCEED_BALANCE_LIMIT(HttpStatus.BAD_REQUEST, "Exceed balance"),
    UNDER_BALANCE_LIMIT(HttpStatus.BAD_REQUEST, "Under balance"),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid charge amount"),
    INVALID_USE_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid use amount"),

    //product
    INSUFFICIENT_PRODUCT_STOCK(HttpStatus.BAD_REQUEST, "Insufficient product stock"),

    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "Invalid user id"),

    //common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
