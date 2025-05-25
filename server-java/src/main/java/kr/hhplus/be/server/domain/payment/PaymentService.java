package kr.hhplus.be.server.domain.payment;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void confirmPayment(Payment payment) {
        payment = paymentRepository.save(payment);
        paymentEventPublisher.publish(PaymentEvent.Completed.of(payment));
    }
}
