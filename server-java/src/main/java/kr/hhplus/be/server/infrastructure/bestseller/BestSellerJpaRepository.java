package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BestSellerJpaRepository extends JpaRepository<BestSeller, Long> {

    List<BestSeller> findTop5ByDateOrderBySalesAmountDesc(LocalDate date);
}
