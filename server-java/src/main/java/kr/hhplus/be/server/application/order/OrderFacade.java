package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.UserRepository;
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
                .map(item -> {
                    Product product = productService.getProductById(item.productId());
                    return Pair.of(product, item.quantity());
                })
                .toList();

        Orders order;
        if(command.useCouponId() == null) {
            order = orderService.createOrder(user, productAndQuantity);
        } else {
            UserCoupon userCoupon = couponService.getUserCouponById(command.useCouponId());
            order = orderService.createOrderWithCoupon(user, userCoupon, productAndQuantity);
            couponService.saveUserCoupon(userCoupon);
        }

        orderService.saveOrder(order);
        productAndQuantity.forEach(pair -> productService.save(pair.getFirst()));

        return new OrderResult(order.getId());
    }

    public OrderResult cancelOrder(CancelOrderCommand command) {
        var order = orderService.getOrderById(command.orderId());

        order.getOrderItems().forEach(item -> {
            var product = productService.getProductById(item.getProductId());
            product.increaseStock(item.getQuantity());
            productService.save(product);
        });

        if(order.isCouponUsed()) {
            var userCoupon = couponService.getUserCouponById(order.getCouponId());
            userCoupon.init();
            couponService.saveUserCoupon(userCoupon);
        }

        order.cancel();
        orderService.saveOrder(order);
        return new OrderResult(order.getId());
    }
}
