package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CancelOrderCommand;
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
     * 주문번호를 생성하여 주문을 생성한 뒤, 주문 항목을 생성하고 주문 취소 스케줄러에 등록한 뒤 주문 취소 스케줄을 등록합니다.
     * TC
     * 예외 케이스는 command와 domain model에서 검증
     */
    public Orders createOrder(CreateOrderCommand command) {
        Orders order = Orders.createWithIdAndUser(orderIdGenerator.gen(), command.userId());
        command.orderItemSpecs().forEach(orderItemSpec ->
            order.addOrderItem(
                    OrderItem.create(order.getId(), orderItemSpec.productId(), orderItemSpec.price(), orderItemSpec.quantity())
            ));

        orderCancelHandler.register(order.getId());
        return orderRepository.saveOrderWithItems(order);
    }

    //TODO 주문 facade
    //TODO 주문 취소
}
