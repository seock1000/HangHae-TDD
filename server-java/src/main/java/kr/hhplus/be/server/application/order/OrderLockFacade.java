package kr.hhplus.be.server.application.order;

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
public class OrderLockFacade {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    private final LockTemplate lockTemplate;

    public OrderResult placeOrder(PlaceOrderCommand command){
        List<LockCommand> locks = new ArrayList<>();
        if(command.useCouponId() != null) {
            locks.add(new LockCommand("userCoupon:" + command.useCouponId(), LockMethod.PUBSUB, 3L, 2L, TimeUnit.SECONDS));
        }
        // 정렬을 통한 데드락 방지
        command.items().stream().sorted(Comparator.comparing(PlaceOrderCommand.PlaceOrderItem::productId))
                .forEach(item -> {
                    locks.add(new LockCommand("product:" + item.productId(), LockMethod.PUBSUB, 3L, 2L, TimeUnit.SECONDS));
                });

        return lockTemplate.executeWithLocks(locks, () -> orderFacade.placeOrder(command));
    }

    public OrderResult cancelOrder(CancelOrderCommand command){
        List<LockCommand> locks = new ArrayList<>();
        locks.add(new LockCommand("order:" + command.orderId(), LockMethod.PUBSUB, 3L, 2L, TimeUnit.SECONDS));
        Orders orders = orderService.getOrderById(command.orderId());
        if(orders.getCouponId() != null) {
            locks.add(new LockCommand("userCoupon:" + orders.getCouponId(), LockMethod.PUBSUB, 3L, 2L, TimeUnit.SECONDS));
        }
        // 정렬을 통한 데드락 방지
        orders.getOrderItems().stream().sorted(Comparator.comparing(OrderItem::getProductId))
                .forEach(item -> {
                    locks.add(new LockCommand("product:" + item.getProductId(), LockMethod.PUBSUB, 3L, 2L, TimeUnit.SECONDS));
                });

        return lockTemplate.executeWithLocks(locks, () -> orderFacade.cancelOrder(command));
    }
}
