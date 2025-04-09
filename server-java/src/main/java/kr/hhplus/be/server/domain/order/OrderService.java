package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderCancelHandler orderCancelHandler;

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
            ).getAmount();
            order.plusTotalAmount(itemAmount);
        });

        orderCancelHandler.register(order.getId());

        return orderRepository.saveOrder(order);
    }
}
