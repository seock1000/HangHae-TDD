package kr.hhplus.be.server.domain.bestseller;

import java.time.LocalDate;
import java.util.List;

public interface BestSellerRepository {

    void saveBaseAll(List<BestSellerBase> bestSellerBases);
    void saveAll(List<BestSeller> bestSellers);

    List<SalesStat> getSalesAmountSumBetween(LocalDate startDate, LocalDate endDate);

    List<BestSeller> getTop5BestSellersByDate(LocalDate date);
}
