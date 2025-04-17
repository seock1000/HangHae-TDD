package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PaymentFacadeTest {

    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private PointJpaRepository pointJpaRepository;

    @Test
    @DisplayName("주문 내역 결제 시, 주문 금액이 보유 포인트보다 작거나 같고 주문 상태가 PENDING이라면 결제에 성공한다.")
    void payWithEnoughPoint() {
        // given
        var order = Instancio.of(Orders.class)
                .set(field("id"), "orderId")
                .set(field("totalAmount"), 10000)
                .set(field("status"), OrderStatus.PENDING)
                .set(field("orderItems"), new ArrayList<>())
                .create();
        orderJpaRepository.saveAndFlush(order);

        var point = Instancio.of(Point.class)
                .set(field("id"), null)
                .set(field("userId"), order.getUser())
                .set(field("balance"), 10000)
                .set(field("histories"), new ArrayList<>())
                .create();
        pointJpaRepository.saveAndFlush(point);

        var command = new PayCommand(order.getId());

        // when
        var result = paymentFacade.pay(command);

        // then
        assertNotNull(result);
    }

}