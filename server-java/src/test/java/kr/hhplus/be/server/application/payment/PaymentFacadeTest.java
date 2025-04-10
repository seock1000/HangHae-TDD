package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.command.UsePointCommand;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {
    @Mock
    private OrderService orderService;
    @Mock
    private PointService pointService;
    @Mock
    private DataPlatformSender dataPlatformSender;
    @InjectMocks
    private PaymentFacade paymentFacade;

    @Test
    @DisplayName("결제 요청 시, 주문을 확정하고 포인트를 차감한 뒤 데이터 플랫폼에 주문 정보를 전송한다.")
    void testPay() {
        // given
        Orders order = Instancio.create(Orders.class);
        UsePointCommand expectedUsePointCommand = new UsePointCommand(order.getUser(), order.getTotalAmount());
        PayCommand command = new PayCommand(order.getId());

        when(orderService.confirmOrder(any())).thenReturn(order);
        when(pointService.use(any())).thenReturn(Instancio.create(Point.class));

        // when
        PaymentResult result = paymentFacade.pay(command);

        // then
        assertNotNull(result);
        assertEquals(order.getId(), result.orderId());

        verify(orderService, times(1)).confirmOrder(command.orderId());
        verify(pointService, times(1)).use(expectedUsePointCommand);
        verify(dataPlatformSender, times(1)).sendOrder(order);
    }

    @Test
    @DisplayName("결제 요청 시, 데이터 플랫폼 전송 중 예외가 발생해도 결제는 정상적으로 진행된다.")
    void testPayWithDataPlatformError() {
        // given
        Orders order = Instancio.create(Orders.class);
        UsePointCommand expectedUsePointCommand = new UsePointCommand(order.getUser(), order.getTotalAmount());
        PayCommand command = new PayCommand(order.getId());

        when(orderService.confirmOrder(any())).thenReturn(order);
        doThrow(new RuntimeException("Data platform error")).when(dataPlatformSender).sendOrder(any());

        // when
        PaymentResult result = paymentFacade.pay(command);

        // then
        assertNotNull(result);
        assertEquals(order.getId(), result.orderId());

        verify(orderService, times(1)).confirmOrder(command.orderId());
        verify(pointService, times(1)).use(expectedUsePointCommand);
        verify(dataPlatformSender, times(1)).sendOrder(order);
    }

}