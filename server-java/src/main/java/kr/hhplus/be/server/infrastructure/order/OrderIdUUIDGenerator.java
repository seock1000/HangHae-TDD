package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderIdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderIdUUIDGenerator implements OrderIdGenerator {
    @Override
    public String gen() {
        return UUID.randomUUID().toString();
    }
}
