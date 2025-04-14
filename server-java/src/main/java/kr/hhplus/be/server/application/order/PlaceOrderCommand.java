package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;

import java.util.List;

public record PlaceOrderCommand(
        long userId,
        Long useCouponId,
        List<PlaceOrderItem> items
) {
    public PlaceOrderCommand {
        if(userId <= 0) {
            throw ApiException.of(ApiError.INVALID_USER_ID);
        }
        if(useCouponId != null && useCouponId <= 0) {
            throw ApiException.of(ApiError.INVALID_COUPON_ID);
        }
        if(items == null || items.isEmpty()) {
            throw ApiException.of(ApiError.INVALID_ORDER_ITEMS);
        }
    }

    record PlaceOrderItem(
            long productId,
            int quantity
    ) {
        public PlaceOrderItem {
            if(productId <= 0) {
                throw ApiException.of(ApiError.INVALID_PRODUCT_ID);
            }
            if(quantity <= 0) {
                throw ApiException.of(ApiError.INVALID_ORDER_QUANTITY);
            }
        }
    }
}
