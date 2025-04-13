package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderIdGenerator orderIdGenerator;

    /**
     * 테스트 필요 없을 듯
     */
    public Orders getOrderById(String orderId) {
        return orderRepository.findOrderById(orderId)
                .orElseThrow(() -> ApiException.of(ApiError.ORDER_NOT_FOUND));
    }

    /**
     * TC
     * 유저 쿠폰이 있으면 쿠폰을 적용한다.
     * 유저 쿠폰이 없으면 쿠폰을 적용하지 않는다.
     */
    public Orders createOrder(User user, List<Pair<Product, Integer>> productAndQuantity) {
        String orderId = orderIdGenerator.gen();

        Orders order = Orders.createWithIdAndUser(orderId, user);
        productAndQuantity.forEach(pair -> {
            Product product = pair.getFirst();
            int quantity = pair.getSecond();
            order.addProduct(product, quantity);
        });

        return order;
    }

    public Orders createOrderWithCoupon(User user, UserCoupon userCoupon, List<Pair<Product, Integer>> productAndQuantity) {
        String orderId = orderIdGenerator.gen();

        Orders order = Orders.createWithIdAndUser(orderId, user);
        productAndQuantity.forEach(pair -> {
            Product product = pair.getFirst();
            int quantity = pair.getSecond();
            order.addProduct(product, quantity);
        });
        if (userCoupon != null) {
            order.applyCoupon(userCoupon);
        }

        return order;
    }

    /**
     * 테스트 필요없을 듯
     */
    public void cancelOrder(Orders order) {
        order.cancel();
    }

    /**
     * 테스트 필요없을 듯
     */
    public void saveOrder(Orders order) {
        orderRepository.saveOrderWithItems(order);
    }
}
