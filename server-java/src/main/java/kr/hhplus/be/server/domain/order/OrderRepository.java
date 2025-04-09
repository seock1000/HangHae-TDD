package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {

    public Orders saveOrder(Orders order);
    public OrderItem saveOrderItem(OrderItem orderItem);
}
