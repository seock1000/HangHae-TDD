package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSellerBase;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BestSellerBaseJpaRepository extends JpaRepository<BestSellerBase, Long> {

    @Query(
            "SELECT new kr.hhplus.be.server.domain.bestseller.SalesStat(b.productId, SUM(b.salesAmount)) " +
                    "FROM BestSellerBase b " +
                    "WHERE b.date BETWEEN :startDate AND :endDate " +
                    "GROUP BY b.productId"
    )
    List<SalesStat> findSalesAmountSumBetween(LocalDate startDate, LocalDate endDate);
}
