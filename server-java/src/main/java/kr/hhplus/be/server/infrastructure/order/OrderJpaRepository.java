package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Orders, String> {
}
