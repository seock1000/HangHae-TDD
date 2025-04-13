package kr.hhplus.be.server.domain.order;

import jakarta.persistence.criteria.Order;
import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class Orders {
    private String id;
    private Long user;
    private Optional<Long> couponId;
    private int totalAmount;
    private int discountAmount;
    private OrderStatus status;
    private List<OrderItem> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Orders(String id, Long user, Optional<Long> couponId, int totalAmount, int discountAmount, OrderStatus status) {
        this.id = id;
        this.user = user;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.couponId = couponId;
        this.status = status;
        this.orderItems = new ArrayList<>();
    }

    public static Orders createWithIdAndUser(String id, User user) {
        return new Orders(id, user.getId(), Optional.empty(), 0, 0, OrderStatus.PENDING);
    }

    public void addProduct(Product product, int quantity) {
        product.decreaseStock(quantity);
        OrderItem orderItem = OrderItem.create(this.id, product, quantity);
        totalAmount += orderItem.getAmount();
        orderItems.add(orderItem);
    }

    /**
     * 이미 적용된 쿠폰이 존재할 시 ORDER_ALREADY_COUPON_APPLIED 예외가 발생한다.
     */
    public void applyCoupon(UserCoupon userCoupon) {
        couponId.ifPresent(id -> {
            throw ApiException.of(ApiError.ORDER_ALREADY_COUPON_APPLIED);
        });
        userCoupon.use();
        this.couponId = Optional.of(userCoupon.getId());
        this.discountAmount = userCoupon.discount(totalAmount);
    }

    public void confirm() {
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw ApiException.of(ApiError.ORDER_CANNOT_BE_CONFIRMED);
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw ApiException.of(ApiError.ORDER_CANNOT_BE_CANCELED);
        }
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 테스트 필요없을 듯
     */
    public boolean isCouponUsed() {
        return couponId.isPresent();
    }

}
