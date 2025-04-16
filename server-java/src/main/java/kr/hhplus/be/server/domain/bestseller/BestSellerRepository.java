package kr.hhplus.be.server.domain.bestseller;

import java.util.List;

public interface BestSellerRepository {

    void saveBaseAll(List<BestSellerBase> bestSellerBases);
    void saveAll(List<BestSeller> bestSellers);

    List<BestSeller> getTopBestSellersBeforeDays(int i, int i1);
}
