package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderSalesAmount;
import kr.hhplus.be.server.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Orders saveOrderWithItems(Orders order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Orders> findOrderById(String orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public List<OrderSalesAmount> findOrderItemSalesAmountByDate(LocalDate targetDate) {
        return orderJpaRepository.findProductSalesAmountByDate(targetDate);
    }
}
