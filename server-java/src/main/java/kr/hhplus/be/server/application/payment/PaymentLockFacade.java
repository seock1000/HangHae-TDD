package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.config.redis.LockCommand;
import kr.hhplus.be.server.config.redis.LockMethod;
import kr.hhplus.be.server.config.redis.LockTemplate;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PaymentLockFacade {

    private final PaymentFacade paymentFacade;
    private final OrderService orderService;

    private final LockTemplate lockTemplate;

    public PayResult pay(PayCommand command) {
        List<LockCommand> locks = new ArrayList<>();
        // order는 충돌이 적을 것으로 예상하여 pub/sub 방식으로 처리
        locks.add(new LockCommand("order:" + command.orderId(), LockMethod.PUBSUB, 5L, 3L, TimeUnit.SECONDS));
        Orders order = orderService.getOrderById(command.orderId());
        // point는 충돌이 적을 것으로 예상하여 pub/sub 방식으로 처리
        locks.add(new LockCommand("point:user:" + order.getUser(), LockMethod.PUBSUB, 5L, 3L, TimeUnit.SECONDS));

        return lockTemplate.executeWithLocks(locks, () -> paymentFacade.pay(command));
    }
}
