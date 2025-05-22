package kr.hhplus.be.server.domain.payment;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentEventPublisher paymentEventPublisher;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 내역을 확정 시에 결제 내역을 저장하고, 결제 이벤트를 발행한다")
    void 결제_내역을_확정하면_결제내역을_저장하고_이벤트_발행() {
        // given
        Payment payment = Instancio.create(Payment.class);
        when(paymentRepository.save(payment)).thenReturn(payment);

        // when
        paymentService.confirmPayment(payment);

        // then
        verify(paymentRepository, times(1)).save(payment);
        verify(paymentEventPublisher, times(1)).publish(any(PaymentEvent.Completed.class));
    }
}