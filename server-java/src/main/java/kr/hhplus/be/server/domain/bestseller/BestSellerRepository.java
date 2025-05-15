package kr.hhplus.be.server.domain.bestseller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BestSellerRepository {

    void saveBaseAll(List<BestSellerBase> bestSellerBases);
    void saveAll(List<BestSeller> bestSellers);

    List<SalesStat> getSalesAmountSumBetween(LocalDate startDate, LocalDate endDate);

    List<BestSeller> getTop5BestSellersByDate(LocalDate date);

    void addDailySalesStat(SalesStat salesStat);

    List<SalesStat> getDailySalesStatByDate(LocalDate date);

    void addLast3DaysSalesStat(SalesStat salesStat);

    List<SalesStat> getTopLast3DaysSalesStat(int limit);
}
