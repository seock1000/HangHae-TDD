package kr.hhplus.be.server.domain.bestseller;

import java.time.LocalDate;
import java.util.List;

public interface BestSellerRepository {

    void saveBaseAll(List<BestSellerBase> bestSellerBases);
    void saveAll(List<BestSeller> bestSellers);

    List<SalesStat> getTopBestSellersBeforeDaysByBase(int i, int i1);

    List<BestSellerProductInfo> getBestSellersByDate(LocalDate date);
}
