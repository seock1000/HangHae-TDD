package kr.hhplus.be.server.domain.payment;

import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RecordApplicationEvents
class PaymentServiceIntegrationTest {

    @MockitoSpyBean
    private PaymentEventPublisher paymentEventPublisher;

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("결제 내역을 확정 시에 결제 내역을 저장하고, 결제 이벤트를 발행한다")
    void 결제_내역을_확정하면_결제내역을_저장하고_이벤트_발행(ApplicationEvents applicationEvents) {
        // given
        Payment payment = Instancio.of(Payment.class)
                .ignore(field(Payment::getId))
                .create();

        // when
        paymentService.confirmPayment(payment);

        // then
        verify(paymentEventPublisher, times(1)).publish(any(PaymentEvent.class));
        assertThat(applicationEvents.stream(PaymentEvent.class)).hasSize(1);
    }
}
