package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.coupon.AppliedCoupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.SoldProduct;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderDataPlatform orderDataPlatform;
    private final OrderCancelHandler orderCancelHandler;

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
    public Orders createOrder(User user, List<Pair<SoldProduct, Integer>> productAndQuantity) {
        String orderId = orderIdGenerator.gen();

        Orders order = Orders.createWithIdAndUser(orderId, user);
        productAndQuantity.forEach(pair -> {
            SoldProduct product = pair.getFirst();
            int quantity = pair.getSecond();
            order.addProduct(product, quantity);
        });

        return order;
    }

    public void registerOrderToCancelHandler(Orders order) {
        orderCancelHandler.register(order.getId());
    }

    /**
     * 테스트 필요없을 듯
     */
    public void saveOrder(Orders order) {
        orderRepository.saveOrderWithItems(order);
    }


    public void sendOrderData(Orders order) {
        try {
            orderDataPlatform.send(order);
        } catch (Exception e) {
            //TODO log?
        }
    }
}
