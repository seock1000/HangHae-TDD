package kr.hhplus.be.server;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {

    //order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order not found"),
    ORDER_CANNOT_BE_CANCELED(HttpStatus.BAD_REQUEST, "Order cannot be canceled"),
    ORDER_CANNOT_BE_CONFIRMED(HttpStatus.BAD_REQUEST, "Order cannot be confirmed"),
    ORDER_ALREADY_COUPON_APPLIED(HttpStatus.BAD_REQUEST, "Order already coupon applied"),
    INVALID_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "Invalid order quantity"),
    INVALID_ORDER_ITEMS(HttpStatus.BAD_REQUEST, "Invalid order items"),
    INVALID_ORDER_ID(HttpStatus.BAD_REQUEST, "Invalid order id"),

    //coupon
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "Coupon not found"),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "Coupon already issued"),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "Coupon expired"),
    COUPON_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "Coupon not issued"),
    COUPON_ALREADY_USED(HttpStatus.BAD_REQUEST, "Coupon already used"),
    INVALID_DISCOUNT_TYPE(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid discount type"),
    INVALID_COUPON_ID(HttpStatus.BAD_REQUEST, "Invalid coupon id"),

    //point
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "Point not found"),
    EXCEED_CHARGE_LIMIT(HttpStatus.BAD_REQUEST, "Exceed charge limit"),
    EXCEED_BALANCE_LIMIT(HttpStatus.BAD_REQUEST, "Exceed balance"),
    UNDER_BALANCE_LIMIT(HttpStatus.BAD_REQUEST, "Under balance"),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid charge amount"),
    INVALID_USE_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid use amount"),

    //product
    INSUFFICIENT_PRODUCT_STOCK(HttpStatus.BAD_REQUEST, "Insufficient product stock"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product not found"),
    INVALID_PRODUCT_ID(HttpStatus.BAD_REQUEST, "Invalid product id"),

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
