package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSellerProductInfo;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BestSellerFacade {

    private final BestSellerService bestSellerService;

    public List<BestSellerProductInfo> getBestSellersByDate(LocalDate date) {
        return bestSellerService.getBestSellersByDate(date);
    }
}
