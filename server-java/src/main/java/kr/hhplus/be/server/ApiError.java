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


    //product
    INSUFFICIENT_PRODUCT_STOCK(HttpStatus.BAD_REQUEST, "Insufficient product stock"),

    //user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),

    //common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
