package kr.hhplus.be.server.domain.order;

public interface OrderCancelHandler {
    void register(String orderId);
}
