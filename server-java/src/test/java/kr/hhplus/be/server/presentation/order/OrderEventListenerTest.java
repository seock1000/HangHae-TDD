package kr.hhplus.be.server.presentation.order;

import kr.hhplus.be.server.application.bestseller.BestSellerFacade;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.payment.PaymentEvent;
import kr.hhplus.be.server.presentation.bestseller.BestSellerEventListener;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@Transactional
@SpringBootTest
@RecordApplicationEvents
class OrderEventListenerTest {

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @MockitoBean
    private OrderFacade orderFacade;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public Executor executor() {
            return new SyncTaskExecutor();
        }
    }

    @Test
    @DisplayName("결제 이벤트 발생시 주문 이벤트 리스너가 호출된다")
    void 결제_이벤트_발생시_주문_이벤트_리스너가_호출된다(ApplicationEvents applicationEvents) {
        // given
        PaymentEvent paymentEvent = Instancio.create(PaymentEvent.class);

        // when
        applicationEventPublisher.publishEvent(paymentEvent);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        verify(orderEventListener).handlePayment(paymentEvent);
        assertThat(applicationEvents.stream(PaymentEvent.class)).hasSize(1);
    }

}