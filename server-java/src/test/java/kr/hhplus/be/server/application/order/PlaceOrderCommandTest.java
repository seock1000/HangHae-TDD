package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.error.InvalidProductError;
import kr.hhplus.be.server.application.order.error.InvalidQuantityError;
import kr.hhplus.be.server.application.order.error.InvalidUserIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderCommandTest {

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 0보다 큰 상품 ID, 수량을 전달하면 상품 ID, 수량을 가진 주문 아이템 DTO가 생성된다.")
    void createOrderItemSpecTest() {
        // given
        long productId = 1L;
        int quantity = 2;

        // when
        PlaceOrderCommand.OrderItemSpec orderItemSpec = new PlaceOrderCommand.OrderItemSpec(productId, quantity);

        // then
        assertNotNull(orderItemSpec);
        assertEquals(productId, orderItemSpec.productId());
        assertEquals(quantity, orderItemSpec.quantity());
    }

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 상품 ID가 0보다 작거나 같으면 InvalidProductError 예외가 발생한다.")
    void createOrderItemSpecWithInvalidProductIdTest() {
        // given
        long productId = 0L;
        int quantity = 2;

        // when, then
        Exception exception = assertThrows(InvalidProductError.class, () -> {
            new PlaceOrderCommand.OrderItemSpec(productId, quantity);
        });
        assertEquals("잘못된 상품 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 수량이 0보다 작거나 같으면 InvalidQuantityError 예외가 발생한다.")
    void createOrderItemSpecWithInvalidQuantityTest() {
        // given
        long productId = 1L;
        int quantity = 0;

        // when, then
        Exception exception = assertThrows(InvalidQuantityError.class, () -> {
            new PlaceOrderCommand.OrderItemSpec(productId, quantity);
        });
        assertEquals("잘못된 수량 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("PlaceOrderCommand 객체를 생성할 때, 0보다 큰 사용자 ID와 주문 상품 목록을 전달하면 사용자 ID와 주문 목록을 가진 주문 DTO가 생성된다.")
    void createPlaceOrderCommandTest() {
        // given
        long userId = 1L;
        PlaceOrderCommand.OrderItemSpec orderItemSpec = new PlaceOrderCommand.OrderItemSpec(1L, 2);
        PlaceOrderCommand command = new PlaceOrderCommand(userId, List.of(orderItemSpec));

        // when, then
        assertNotNull(command);
        assertEquals(userId, command.userId());
        assertEquals(1, command.orderItemSpecs().size());
        assertEquals(orderItemSpec, command.orderItemSpecs().get(0));
    }

    @Test
    @DisplayName("PlaceOrderCommand 객체를 생성할 때, 사용자 ID가 0보다 작거나 같으면 InvalidUserIdError 예외가 발생한다.")
    void createPlaceOrderCommandWithInvalidUserIdTest() {
        // given
        long userId = 0L;
        PlaceOrderCommand.OrderItemSpec orderItemSpec = new PlaceOrderCommand.OrderItemSpec(1L, 2);

        // when, then
        Exception exception = assertThrows(InvalidUserIdError.class, () -> {
            new PlaceOrderCommand(userId, List.of(orderItemSpec));
        });
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("PlaceOrderCommand 객체를 생성할 때, 주문 항목이 비어있으면 InvalidOrderItemError 예외가 발생한다.")
    void createPlaceOrderCommandWithEmptyOrderItemTest() {
        // given
        long userId = 1L;

        // when, then
        Exception exception = assertThrows(InvalidUserIdError.class, () -> {
            new PlaceOrderCommand(userId, List.of());
        });
        assertEquals("주문 항목이 비어있습니다.", exception.getMessage());
    }

}