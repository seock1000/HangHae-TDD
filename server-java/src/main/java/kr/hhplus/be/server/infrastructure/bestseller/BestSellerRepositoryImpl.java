package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BestSellerRepositoryImpl implements BestSellerRepository {

    private final BestSellerJpaRepository bestSellerJpaRepository;
    private final BestSellerBaseJpaRepository bestSellerBaseJpaRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveBaseAll(List<BestSellerBase> bestSellerBases) {
        bestSellerBaseJpaRepository.saveAll(bestSellerBases);
    }

    @Override
    public void saveAll(List<BestSeller> bestSellers) {
        bestSellerJpaRepository.saveAll(bestSellers);
    }

    @Override
    public List<SalesStat> getSalesAmountSumBetween(LocalDate startDate, LocalDate endDate) {
        return bestSellerBaseJpaRepository.findSalesAmountSumBetween(startDate, endDate);
    }

    @Override
    public List<BestSellerProductInfo> getTopBestSellersByDate(int limit, LocalDate date) {
        String sql = """
                SELECT b.product_id, p.title, p.description, p.stock, b.sales_amount
                FROM best_seller b
                JOIN product p ON b.product_id = p.id
                WHERE b.date = ?
                ORDER BY b.sales_amount DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new BestSellerProductInfo(
                    rs.getLong("product_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getInt("stock"),
                    rs.getInt("sales_amount")
            );
        }, date, limit);
    }
}
