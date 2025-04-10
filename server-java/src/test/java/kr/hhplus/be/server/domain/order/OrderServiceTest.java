package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CancelOrderCommand;
import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.error.OrderNotExistError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderIdGenerator orderIdGenerator;
    @Mock
    private OrderCancelHandler orderCancelHandler;
    @InjectMocks
    private OrderService orderService;

    /**
     * 값 연산은 도메인 모델에서 검증했기때문에 mock 만 진행
     */
    @Test
    @DisplayName("주문 생성 시 유효한 CreateOrderCommand를 전달하면 주문과 주문 아이템을 생성하고 저장, 미결제시 주문 취소를 하는 handler에 등록한다.")
    void createOrderTest() {
        // given
        CreateOrderCommand command = new CreateOrderCommand(1L, List.of(
                new CreateOrderCommand.OrderItemSpec(1L, 1000, 2),
                new CreateOrderCommand.OrderItemSpec(2L, 2000, 3)
        ));

        when(orderIdGenerator.gen()).thenReturn("orderId");
        when(orderRepository.saveOrderWithItems(any())).thenReturn(Orders.createWithIdAndUser("orderId", 1L));

        // when
        Orders order = orderService.placeOrder(command);

        // then
        assertNotNull(order);
        verify(orderRepository, times(1)).saveOrderWithItems(any());
        verify(orderCancelHandler, times(1)).register(any());
    }

    @Test
    @DisplayName("주문 취소 시 유효한 CancelOrderCommand를 전달하면 주문을 취소하고 저장한다.")
    void cancelOrderTest() {
        // given
        CancelOrderCommand command = new CancelOrderCommand("orderId");
        Orders order = Orders.createWithIdAndUser("orderId", 1L);
        when(orderRepository.findOrderById(command.orderId())).thenReturn(java.util.Optional.of(order));
        when(orderRepository.saveOrder(any())).thenReturn(order);

        // when
        Orders canceledOrder = orderService.cancelByHandler(command);

        // then
        assertNotNull(canceledOrder);
        verify(orderRepository, times(1)).saveOrder(any());
    }

    @Test
    @DisplayName("주문 취소 시 주문이 존재하지 않으면 OrderNotExistError 예외를 발생시킨다.")
    void cancelOrderNotExistTest() {
        // given
        CancelOrderCommand command = new CancelOrderCommand("orderId");
        when(orderRepository.findOrderById(command.orderId())).thenReturn(java.util.Optional.empty());

        // when, then
        assertThrows(OrderNotExistError.class, () -> orderService.cancelByHandler(command));
        verify(orderRepository, times(0)).saveOrder(any());
    }

}