package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final UserService userService;
    private final CouponService couponService;
    private final ProductService productService;

    public OrderResult placeOrder(PlaceOrderCommand command) {
        var user = userService.getUserById(command.userId());
        List<Pair<Product, Integer>> productAndQuantity = command.items().stream()
                .map(it -> Pair.of(productService.getProductByIdForUpdate(it.productId()), it.quantity()))
                .toList();

        Orders order = orderService.createOrder(user, productAndQuantity.stream()
                .map(it -> Pair.of(it.getFirst().toOrderedProduct(), it.getSecond()))
                .toList());

        if (command.useCouponId() != null) {
            var coupon = couponService.getUserCouponById(command.useCouponId());
            order.applyCoupon(coupon.toAppliedCoupon());
            couponService.saveUserCoupon(coupon);
        }
        productAndQuantity.forEach(pair -> productService.save(pair.getFirst()));
        orderService.saveOrder(order);

        orderService.registerOrderToCancelHandler(order);

        return new OrderResult(order.getId());
    }

    public OrderResult cancelOrder(CancelOrderCommand command) {
        var order = orderService.getOrderById(command.orderId());
        order.cancel();
        orderService.saveOrder(order);

        order.getOrderItems().forEach(item -> {
            var product = productService.getProductByIdForUpdate(item.getProductId());
            product.increaseStock(item.getQuantity());
            productService.save(product);
        });

        if(order.isCouponUsed()) {
            var userCoupon = couponService.getUserCouponById(order.getCouponId());
            userCoupon.cancelUse();
            couponService.saveUserCoupon(userCoupon);
        }

        return new OrderResult(order.getId());
    }
}
