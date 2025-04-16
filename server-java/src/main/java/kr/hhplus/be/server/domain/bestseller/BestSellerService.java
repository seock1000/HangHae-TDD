package kr.hhplus.be.server.domain.bestseller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<BestSeller> getTop5BestSellersBefore3Days() {
        return bestSellerRepository.getTopBestSellersBeforeDays(5, 3);
    }
}
