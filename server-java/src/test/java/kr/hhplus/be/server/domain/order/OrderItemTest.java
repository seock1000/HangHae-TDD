package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    @DisplayName("주문 아이템 생성 시, 주문번호와 상품 ID, 가격, 수량을 전달하면 주문번호, 상품 ID, 총액, 가격, 수량을 가진 주문 아이템이 생성된다.")
    void createOrderItemTest() {
        // given
        String orderId = "orderId";
        Long productId = 1L;
        int price = 1000;
        int quantity = 2;
        int amount = price * quantity;

        // when
        OrderItem orderItem = OrderItem.create(orderId, productId, price, quantity);

        // then
        assertNotNull(orderItem);
        assertEquals(orderId, orderItem.getOrdersId());
        assertEquals(productId, orderItem.getProductId());
        assertEquals(amount, orderItem.getAmount());
        assertEquals(price, orderItem.getPrice());
        assertEquals(quantity, orderItem.getQuantity());
    }

}