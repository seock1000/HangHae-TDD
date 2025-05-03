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

    public List<SalesStat> getSalesStatBetween(LocalDate startDate, LocalDate endDate) {
        return bestSellerRepository.getSalesAmountSumBetween(startDate, endDate);
    }

    public List<BestSeller> getTodayTop5BestSellers() {
        return bestSellerRepository.getTop5BestSellersByDate(LocalDate.now());
    }
}
