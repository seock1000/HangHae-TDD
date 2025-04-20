package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    public Orders saveOrderWithItems(Orders order);
    public Optional<Orders> findOrderById(String orderId);
    public List<OrderSalesAmount> findOrderItemSalesAmountByDate(LocalDate targetDate);
}
