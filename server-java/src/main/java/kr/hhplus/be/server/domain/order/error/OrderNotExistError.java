package kr.hhplus.be.server.domain.order.error;

public class OrderNotExistError extends RuntimeException {
    public static OrderNotExistError of(String message) {
        return new OrderNotExistError(message);
    }

    private OrderNotExistError(String message) {
        super(message);
    }
}
