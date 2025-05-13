package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.domain.bestseller.BestSellerBase;
import kr.hhplus.be.server.domain.bestseller.BestSellerService;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
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
     * 23:40에 당일의 판매량을 기준으로 베스트셀러를 생성한다.
     */
    @Deprecated
    @Scheduled(cron = "0 40 23 * * ?")
    public void saveTodaySalesAmounts() {
        var targetDate = LocalDate.now();

        // 오늘의 판매량을 가져온다.
        var salesAmounts = orderService.getSalesAmountByDate(targetDate);
        // 오늘의 판매량을 기준으로 내일 표출될 베스트셀러 베이스를 생성한다.
        var bestSellerBases = salesAmounts.stream()
                .map(it -> BestSellerBase.createWithSalesAmountAndDate(it, targetDate.plusDays(1)))
                .toList();
        bestSellerService.saveBaseAll(bestSellerBases);

        // 어제, 오늘, 내일자 집계 판매량을 가져온다.
        var salesStats = bestSellerService.getSalesStatBetween(targetDate.minusDays(1), targetDate.plusDays(1));
        // 3일치 판매량을 기준으로 내일 표출될 베스트셀러를 생성한다.
        var bestSellers = salesStats.stream()
                .map(it -> BestSeller.createWithSalesStatAndDate(it, targetDate.plusDays(1)))
                .toList();
        bestSellerService.saveAll(bestSellers);
    }

    /**
     * 베스트 셀러 통계기능
     * 매일 00:00에 어제까지의 3일치 판매량을 기준으로 베스트셀러를 생성한다.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveTodaySalesAmountsV2() {
        var startDate = LocalDate.now().minusDays(3);
        var endDate = LocalDate.now().minusDays(1);

        for(LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            bestSellerService.getDailySalesStatByDate(date)
                    .forEach(bestSellerService::updateDailySalesStat);
        }
    }
}
