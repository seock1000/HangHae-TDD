package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CancelOrderCommand;
import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.error.OrderNotExistError;
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

    /**
     * 주문 취소
     * 주문 취소는 주문이 존재하는지 확인한 뒤, 주문을 취소하고 저장합니다.
     * TC
     * 주문이 존재하지 않는 경우 예외를 발생시킵니다.
     * 주문 취소는 주문 도메인에서 처리합니다.
     */
    public Orders cancelByHandler(CancelOrderCommand command) {
        Orders order = orderRepository.findOrderById(command.orderId())
                .orElseThrow(() -> OrderNotExistError.of("주문이 존재하지 않습니다."));

        order.cancel();
        return orderRepository.saveOrder(order);
    }

    /**
     * 주문 확정
     * 주문 확정은 주문이 존재하는지 확인한 뒤, 주문을 확정하고 저장합니다.
     * TC
     * 주문이 존재하지 않는 경우 예외를 발생시킵니다.
     */
    public Orders confirmOrder(String orderId) {
        Orders order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> OrderNotExistError.of("주문이 존재하지 않습니다."));

        order.confirm();
        return orderRepository.saveOrder(order);
    }
}
