package kr.hhplus.be.server.application.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderLockFacade {

    private final OrderFacade orderFacade;

    public OrderResult placeOrderWithLock(PlaceOrderCommand command) {
        return null;
    }
}
