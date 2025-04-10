package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.order.command.CancelOrderHandlerCommand;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {
    @Mock
    private OrderService orderService;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderFacade orderFacade;


    @Test
    @DisplayName("상품 주문 시, 재고를 차감하고 유저를 조회한 뒤 주문을 생성한다.")
    void testPlaceOrder() {
        // given
        PlaceOrderCommand command = new PlaceOrderCommand(1L, List.of(
                new PlaceOrderCommand.OrderItemSpec(1L, 2),
                new PlaceOrderCommand.OrderItemSpec(2L, 3)
        ));
        when(productService.decreaseStock(any()))
                .thenReturn(new Product(1L, "product", "description", 1000, 10))
                .thenReturn(new Product(2L, "product", "description", 2000, 20));
        when(userService.getUserById(any())).thenReturn(new User(1L));
        when(orderService.createOrder(any())).thenReturn(Orders.createWithIdAndUser("orderId", 1L));

        // when
        PlaceOrderResult result = orderFacade.placeOrder(command);

        // then

        verify(productService, times(2)).decreaseStock(any());
        verify(userService, times(1)).getUserById(any());
        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    @DisplayName("주문 취소 시, 주문을 취소하고 가점유했던만큼 상품의 재고를 증가시킨다.")
    void testCancelOrderByHandler() {
        // given
        CancelOrderHandlerCommand command = new CancelOrderHandlerCommand("orderId");

        Orders order = Orders.createWithIdAndUser("orderId", 1L);
        List.of(OrderItem.create(order.getId(), 1L, 1000, 2),
                OrderItem.create(order.getId(), 2L, 1000, 2)
        ).forEach(order::addOrderItem);

        when(orderService.cancelByHandler(any())).thenReturn(order);

        // when
        orderFacade.cancelOrderByHandler(command);

        // then
        verify(orderService, times(1)).cancelByHandler(command);
        verify(productService, times(2)).increaseStock(any());
    }

}