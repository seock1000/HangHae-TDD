package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.domain.bestseller.BestSellerBase;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class BestSellerScheduledFacade {

    private final BestSellerService bestSellerService;
    private final OrderService orderService;

    /**
     * 베스트 셀러 통계기능
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveYesterdaySalesAmounts() {
        var targetDate = LocalDate.now().minusDays(1);

        // 어제의 판매량을 가져온다.
        var salesAmounts = orderService.getSalesAmountByDate(targetDate);
        // 어제의 판매량을 기준으로 베스트셀러 베이스를 생성한다.
        var bestSellerBases = salesAmounts.stream()
                .map(it -> BestSellerBase.createWithSalesAmountAndDate(it, targetDate))
                .toList();
        bestSellerService.saveBaseAll(bestSellerBases);

        // 어제까지의 3일치 판매량을 가져온다.
        var salesStats = bestSellerService.getSalesStatBetween(targetDate.minusDays(2), targetDate);
        // 3일치 판매량을 기준으로 베스트셀러를 생성한다.
        var bestSellers = salesStats.stream()
                .map(it -> BestSeller.createWithSalesStatAndDate(it, targetDate))
                .toList();
        bestSellerService.saveAll(bestSellers);
    }
}
