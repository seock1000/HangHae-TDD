package kr.hhplus.be.server.domain.order.command;

import kr.hhplus.be.server.domain.order.error.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderCommandTest {

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 0보다 큰 상품 ID, 가격, 수량을 전달하면 상품 ID, 가격, 수량을 가진 주문 아이템 DTO가 생성된다.")
    void createOrderItemSpecTest() {
        // given
        Long productId = 1L;
        int price = 1000;
        int quantity = 2;

        // when
        CreateOrderCommand.OrderItemSpec orderItemSpec = new CreateOrderCommand.OrderItemSpec(productId, price, quantity);

        // then
        assertNotNull(orderItemSpec);
        assertEquals(productId, orderItemSpec.productId());
        assertEquals(price, orderItemSpec.price());
        assertEquals(quantity, orderItemSpec.quantity());
    }

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 상품 ID가 0보다 작거나 같으면 InvalidProductError 예외가 발생한다.")
    void createOrderItemSpecWithInvalidProductIdTest() {
        // given
        Long productId = 0L;
        int price = 1000;
        int quantity = 2;

        // when, then
        Exception exception = assertThrows(InvalidProductError.class, () -> {
            new CreateOrderCommand.OrderItemSpec(productId, price, quantity);
        });
        assertEquals("잘못된 상품 ID 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 가격이 0보다 작거나 같으면 InvalidPriceError 예외가 발생한다.")
    void createOrderItemSpecWithInvalidPriceTest() {
        // given
        Long productId = 1L;
        int price = 0;
        int quantity = 2;

        // when, then
        Exception exception = assertThrows(InvalidPriceError.class, () -> {
            new CreateOrderCommand.OrderItemSpec(productId, price, quantity);
        });
        assertEquals("잘못된 가격 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("OrderItemSpec 객체를 생성할 때, 수량이 0보다 작거나 같으면 InvalidQuantityError 예외가 발생한다.")
    void createOrderItemSpecWithInvalidQuantityTest() {
        // given
        Long productId = 1L;
        int price = 1000;
        int quantity = 0;

        // when, then
        Exception exception = assertThrows(InvalidQuantityError.class, () -> {
            new CreateOrderCommand.OrderItemSpec(productId, price, quantity);
        });
        assertEquals("잘못된 수량 형식입니다.", exception.getMessage());
    }


    @Test
    @DisplayName("CreateOrderCommand 객체를 생성할 때, 사용자 ID와 주문 항목을 전달하면 CreateOrderCommand 객체가 생성된다.")
    void createOrderCommandTest() {
        // given
        long userId = 1L;
        CreateOrderCommand.OrderItemSpec orderItemSpec1 = new CreateOrderCommand.OrderItemSpec(1L, 1000, 2);
        CreateOrderCommand.OrderItemSpec orderItemSpec2 = new CreateOrderCommand.OrderItemSpec(2L, 2000, 3);

        //when
        CreateOrderCommand command = new CreateOrderCommand(userId, List.of(orderItemSpec1, orderItemSpec2));

        // then
        assertNotNull(command);
        assertEquals(userId, command.userId());
        assertEquals(2, command.orderItemSpecs().size());
        assertIterableEquals(List.of(orderItemSpec1, orderItemSpec2), command.orderItemSpecs());
    }

    @Test
    @DisplayName("CreateOrderCommand 객체를 생성할 때, 사용자 ID가 0보다 작거나 같으면 InvalidUserError 예외가 발생한다.")
    void createOrderCommandWithInvalidUserIdTest() {
        // given
        long userId = 0L;
        CreateOrderCommand.OrderItemSpec orderItemSpec1 = new CreateOrderCommand.OrderItemSpec(1L, 1000, 2);
        CreateOrderCommand.OrderItemSpec orderItemSpec2 = new CreateOrderCommand.OrderItemSpec(2L, 2000, 3);

        // when, then
        Exception exception = assertThrows(InvalidUserError.class, () -> {
            new CreateOrderCommand(userId, List.of(orderItemSpec1, orderItemSpec2));
        });
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }


    @Test
    @DisplayName("CreateOrderCommand 객체를 생성할 때, 주문 항목이 null이거나 비어있으면 InvalidOrderItemError 예외가 발생한다.")
    void createOrderCommandWithInvalidOrderItemTest() {
        // given
        long userId = 1L;

        // when, then
        Exception exception = assertThrows(InvalidOrderItemError.class, () -> {
            new CreateOrderCommand(userId, null);
        });
        assertEquals("주문 항목이 비어있습니다.", exception.getMessage());

        exception = assertThrows(InvalidOrderItemError.class, () -> {
            new CreateOrderCommand(userId, List.of());
        });
        assertEquals("주문 항목이 비어있습니다.", exception.getMessage());
    }
}