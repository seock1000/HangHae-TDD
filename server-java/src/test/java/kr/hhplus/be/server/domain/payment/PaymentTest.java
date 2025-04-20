package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.PendingOrder;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentTest {

    @Test
    @DisplayName("포인트 결제 내역 생성 시, order와 point를 받아 포인트 결제 내역을 생성한다")
    void createPaymentWithPoint() {
        // given
        PendingOrder order = Mockito.mock(PendingOrder.class);
        UserPoint point = Mockito.mock(UserPoint.class);
        when(order.getTotalAmount()).thenReturn(1000);
        when(order.getId()).thenReturn("orderId");

        // when
        Payment payment = Payment.payWithPoint(order, point);

        // then
        verify(point, times(1)).use(1000);
        verify(order, times(1)).confirm();
        assertEquals("orderId", payment.getOrderId());
        assertEquals(PaymentMethod.POINT, payment.getMethod());
        assertEquals(1000, payment.getAmount());
    }

}