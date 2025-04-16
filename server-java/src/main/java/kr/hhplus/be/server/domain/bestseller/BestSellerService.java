package kr.hhplus.be.server.domain.bestseller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestSellerService {

    private final BestSellerRepository bestSellerRepository;

    public void saveBaseAll(List<BestSellerBase> bestSellerBases) {
        bestSellerRepository.saveBaseAll(bestSellerBases);
    }

    public void saveAll(List<BestSeller> bestSellers) {
        bestSellerRepository.saveAll(bestSellers);
    }

    public List<SalesStat> getTop5SalesStatBefore3Days() {
        return bestSellerRepository.getTopBestSellersBeforeDaysByBase(5, 3);
    }

    public List<BestSellerProductInfo> getBestSellersByDate(LocalDate date) {
        return bestSellerRepository.getBestSellersByDate(date);
    }
}
