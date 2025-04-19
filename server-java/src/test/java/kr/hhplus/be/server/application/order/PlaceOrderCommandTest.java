package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderCommandTest {


    @Test
    @DisplayName("유효한 productId와 quantity로 PlaceOrderCommand.PlaceOrderItem을 생성하면 성공한다.")
    void validPlaceOrderItem() {
        long productId = 1L;
        int quantity = 2;

        PlaceOrderCommand.PlaceOrderItem item = new PlaceOrderCommand.PlaceOrderItem(productId, quantity);

        assertEquals(productId, item.productId());
        assertEquals(quantity, item.quantity());
    }

    @Test
    @DisplayName("productId가 0 이하인 경우 PlaceOrderCommand.PlaceOrderItem을 생성하면 ApiException(INVALID_PRODUCT_ID)이 발생한다.")
    void invalidProductId() {
        long productId = 0L;
        int quantity = 2;

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand.PlaceOrderItem(productId, quantity));

        assertEquals(ApiError.INVALID_PRODUCT_ID, exception.getApiError());
    }

    @Test
    @DisplayName("quantity가 0 이하인 경우 PlaceOrderCommand.PlaceOrderItem을 생성하면 ApiException(INVALID_ORDER_QUANTITY)이 발생한다.")
    void invalidQuantity() {
        long productId = 1L;
        int quantity = 0;

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand.PlaceOrderItem(productId, quantity));

        assertEquals(ApiError.INVALID_ORDER_QUANTITY, exception.getApiError());
    }

    @Test
    @DisplayName("유효한 userId와 PlaceOrderItem으로 PlaceOrderCommand를 생성하면 성공한다.")
    void validPlaceOrderCommand() {
        long userId = 1L;
        Long useCouponId = null;
        PlaceOrderCommand.PlaceOrderItem item = Instancio.create(PlaceOrderCommand.PlaceOrderItem.class);
        PlaceOrderCommand command = new PlaceOrderCommand(userId, useCouponId, List.of(item));

        assertEquals(userId, command.userId());
        assertNull(command.useCouponId());
        assertEquals(1, command.items().size());
    }

    @Test
    @DisplayName("userId가 0 이하인 경우 PlaceOrderCommand를 생성하면 ApiException(INVALID_USER_ID)이 발생한다.")
    void invalidUserId() {
        long userId = 0L;
        Long useCouponId = null;
        PlaceOrderCommand.PlaceOrderItem item = Instancio.create(PlaceOrderCommand.PlaceOrderItem.class);

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand(userId, useCouponId, List.of(item)));

        assertEquals(ApiError.INVALID_USER_ID, exception.getApiError());
    }

    @Test
    @DisplayName("useCouponId가 있으면서, 0 이하인 경우 PlaceOrderCommand를 생성하면 ApiException(INVALID_COUPON_ID)이 발생한다.")
    void invalidUseCouponId() {
        long userId = 1L;
        Long useCouponId = 0L;
        PlaceOrderCommand.PlaceOrderItem item = Instancio.create(PlaceOrderCommand.PlaceOrderItem.class);

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand(userId, useCouponId, List.of(item)));

        assertEquals(ApiError.INVALID_COUPON_ID, exception.getApiError());
    }

    @Test
    @DisplayName("items가 null인 경우 PlaceOrderCommand를 생성하면 ApiException(INVALID_ORDER_ITEMS)이 발생한다.")
    void nullItems() {
        long userId = 1L;
        Long useCouponId = null;

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand(userId, useCouponId, null));

        assertEquals(ApiError.INVALID_ORDER_ITEMS, exception.getApiError());
    }

    @Test
    @DisplayName("items가 비어있는 경우 PlaceOrderCommand를 생성하면 ApiException(INVALID_ORDER_ITEMS)이 발생한다.")
    void emptyItems() {
        long userId = 1L;
        Long useCouponId = null;

        ApiException exception = assertThrows(ApiException.class, () -> new PlaceOrderCommand(userId, useCouponId, List.of()));

        assertEquals(ApiError.INVALID_ORDER_ITEMS, exception.getApiError());
    }
}