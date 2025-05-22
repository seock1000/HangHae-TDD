package kr.hhplus.be.server.presentation.bestseller;

import kr.hhplus.be.server.application.bestseller.BestSellerFacade;
import kr.hhplus.be.server.domain.payment.PaymentEvent;
import kr.hhplus.be.server.presentation.order.OrderEventListener;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
@RecordApplicationEvents
@ActiveProfiles("test")
class BestSellerEventListenerTest {

    @MockitoBean
    private BestSellerFacade bestSellerFacade;

    @MockitoBean
    private OrderEventListener orderEventListener;

    @MockitoSpyBean
    private BestSellerEventListener bestSellerEventListener;

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
    @DisplayName("결제 이벤트 발생시 베스트셀러 이벤트 리스너가 호출된다")
    void 결제_이벤트_발생시_베스트셀러_이벤트_리스너가_호출된다(ApplicationEvents applicationEvents) {
        // given
        PaymentEvent.Completed paymentEvent = Instancio.create(PaymentEvent.Completed.class);

        // when
        applicationEventPublisher.publishEvent(paymentEvent);

        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        verify(bestSellerEventListener, times(1)).handlePayment(any());
        assertThat(applicationEvents.stream(PaymentEvent.Completed.class)).hasSize(1);
    }

}