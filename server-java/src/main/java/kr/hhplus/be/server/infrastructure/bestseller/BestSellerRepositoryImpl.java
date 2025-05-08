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
    public List<BestSeller> getTop5BestSellersByDate(LocalDate date) {
        return bestSellerJpaRepository.findTop5ByDateOrderBySalesAmountDesc(date);
    }
}
