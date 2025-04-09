package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.command.CreateOrderCommand;
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
    @DisplayName("주문 생성 시 유효한 CreateOrderCommand를 전달하면 주문과 주문 아이템을 생성하고 저장한다.")
    void createOrderTest() {
        // given
        String orderId = "orderId";
        Long userId = 1L;
        CreateOrderCommand command = new CreateOrderCommand(userId, List.of(
                new CreateOrderCommand.OrderItemSpec(1L, 1000, 2),
                new CreateOrderCommand.OrderItemSpec(2L, 2000, 3)
        ));

        when(orderIdGenerator.gen()).thenReturn(orderId);
        //어차피 mock 검증이라 아무거나 반환
        when(orderRepository.saveOrderItem(any())).thenReturn(OrderItem.create(orderId, 1L, 1000, 2));

        // when
        Orders order = orderService.createOrder(command);

        // then
        verify(orderRepository, times(2)).saveOrderItem(any());
        verify(orderRepository, times(1)).saveOrder(any());
        verify(orderCancelHandler, times(1)).register(orderId);
    }

}