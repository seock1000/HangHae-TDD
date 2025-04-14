package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
