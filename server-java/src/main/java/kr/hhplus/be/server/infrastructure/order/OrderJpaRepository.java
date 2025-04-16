package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderSalesAmount;
import kr.hhplus.be.server.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Orders, String> {

    @Query(
            "SELECT new kr.hhplus.be.server.domain.order.OrderSalesAmount(oi.productId, SUM(oi.quantity))" +
                    "FROM Orders o INNER JOIN o.orderItems oi " +
                    "WHERE o.orderDate = :targetDate AND o.status = kr.hhplus.be.server.domain.order.OrderStatus.CONFIRMED " +
                    "GROUP BY oi.productId"
    )
    List<OrderSalesAmount> findProductSalesAmountByDate(@Param("targetDate") LocalDate targetDate);
}
