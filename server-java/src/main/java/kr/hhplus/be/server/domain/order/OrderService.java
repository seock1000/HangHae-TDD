package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.application.order.OrderEventPublisher;
import kr.hhplus.be.server.domain.product.OrderedProduct;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderDataPlatform orderDataPlatform;
    private final OrderCancelHandler orderCancelHandler;
    private final OrderEventPublisher orderEventPublisher;

    /**
     * 테스트 필요 없을 듯
     */
    public Orders getOrderById(String orderId) {
        return orderRepository.findOrderById(orderId)
                .orElseThrow(() -> ApiException.of(ApiError.ORDER_NOT_FOUND));
    }

    public List<OrderSalesAmount> getSalesAmountByDate(LocalDate targetDate) {
        return orderRepository.findOrderItemSalesAmountByDate(targetDate);
    }

    /**
     * TC
     * 유저 쿠폰이 있으면 쿠폰을 적용한다.
     * 유저 쿠폰이 없으면 쿠폰을 적용하지 않는다.
     */
    public Orders createOrder(User user, List<Pair<OrderedProduct, Integer>> productAndQuantity) {
        String orderId = orderIdGenerator.gen();

        Orders order = Orders.createWithIdAndUser(orderId, user);
        productAndQuantity.forEach(pair -> {
            OrderedProduct product = pair.getFirst();
            int quantity = pair.getSecond();
            order.addProduct(product, quantity);
        });

        return order;
    }

    public void registerOrderToCancelHandler(Orders order) {
        orderCancelHandler.register(order.getId());
    }

    public void removeOrderToCancelHandler(Orders order) {
        orderCancelHandler.delete(order.getId());
    }

    /**
     * 테스트 필요없을 듯
     */
    public void saveOrder(Orders order) {
        orderRepository.saveOrderWithItems(order);
    }


    public void confirmOrder(Orders order) {
        //order.confirm(); // paymentService에서 이미 호출함
        orderCancelHandler.delete(order.getId());
        orderRepository.saveOrderWithItems(order);
        orderEventPublisher.publishConfirmEvent(OrderEvent.Confirmed.of(order));
    }
}
