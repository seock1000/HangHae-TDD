package kr.hhplus.be.server.infrastructure.bestseller;

import kr.hhplus.be.server.domain.bestseller.SalesStat;

import java.time.LocalDate;
import java.util.List;

public interface BestSellerRedisRepository {

    void addDailySalesStat(SalesStat salesStat);

    List<SalesStat> findDailySalesStatByDate(LocalDate date);

    void addLast3DaysSalesStat(SalesStat salesStat);

    List<SalesStat> findTopLast3DaysSalesStat(int limit);
}
