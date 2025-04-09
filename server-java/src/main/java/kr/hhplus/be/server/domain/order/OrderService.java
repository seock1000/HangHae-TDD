package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;

    /**
     * 주문 생성
     * 주문번호를 생성하고, 주문을 생성한 뒤, 주문 항목을 생성합니다.
     * TC
     * 예외 케이스는 command와 domain model에서 검증
     */
    public Orders createOrder(CreateOrderCommand command) {
        String orderId = orderIdGenerator.gen();
        Orders order = Orders.createWithIdAndUser(orderId, command.userId());

        command.orderItemSpecs().forEach(orderItemSpec -> {
            int itemAmount = orderRepository.saveOrderItem(
                    OrderItem.create(orderId, orderItemSpec.productId(), orderItemSpec.price(), orderItemSpec.quantity())
            ).amount;
            order.plusTotalAmount(itemAmount);
        });

        return orderRepository.saveOrder(order);
    }
}
