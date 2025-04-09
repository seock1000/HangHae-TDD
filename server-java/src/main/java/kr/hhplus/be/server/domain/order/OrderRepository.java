package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {

    public Orders saveOrder(Orders order);
    public List<OrderItem> saveOrderItems(List<OrderItem> orderItems);
}
