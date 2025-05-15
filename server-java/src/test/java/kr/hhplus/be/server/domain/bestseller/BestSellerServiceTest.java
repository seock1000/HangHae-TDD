package kr.hhplus.be.server.domain.bestseller;

import kr.hhplus.be.server.IntegrationTestSupport;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class BestSellerServiceTest extends IntegrationTestSupport {

    @Autowired
    private BestSellerService bestSellerService;

    @Test
    @DisplayName("판매량을 저장했을 때, 기존 저장된 판매량이 없으면 새로 저장한다.")
    void 판매량을_저장시_기존_판매량이_없으면_생성() {
        // given
        var salesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 10)
                .create();

        // when
        bestSellerService.updateDailySalesStat(salesStat);

        // then
        var result = bestSellerService.getDailySalesStatByDate(LocalDate.now());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(salesStat.getProductId(), result.get(0).getProductId());
        assertEquals(salesStat.getAmount(), result.get(0).getAmount());
    }

    @Test
    @DisplayName("판매량을 저장했을 때, 기존 저장된 판매량이 있으면 증가한다.")
    void 판매량을_저장시_기존_판매량이_있으면_증가() {
        // given
        var salesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 10)
                .create();
        bestSellerService.updateDailySalesStat(salesStat);

        // when
        var newSalesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 5)
                .create();
        bestSellerService.updateDailySalesStat(newSalesStat);

        // then
        var result = bestSellerService.getDailySalesStatByDate(LocalDate.now());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(salesStat.getProductId(), result.get(0).getProductId());
        assertEquals(salesStat.getAmount() + newSalesStat.getAmount(), result.get(0).getAmount());
    }

    @Test
    @DisplayName("날짜로 해당 일자에 집계된 판매량을 모두 조회한다.")
    void 날짜를_키로_해당_일자에_집계된_판매량_조회() {
        // given
        var salesStat1 = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 10)
                .create();
        var salesStat2 = Instancio.of(SalesStat.class)
                .set(field("productId"), 2L)
                .set(field("amount"), 20)
                .create();
        bestSellerService.updateDailySalesStat(salesStat1);
        bestSellerService.updateDailySalesStat(salesStat2);

        // when
        var result = bestSellerService.getDailySalesStatByDate(LocalDate.now());

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("최근 3일 판매량을 저장했을 때, 기존 저장된 판매량이 없으면 새로 저장한다.")
    void 최근_3일_판매량을_저장시_기존_판매량이_없으면_생성() {
        // given
        var salesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 10)
                .create();

        // when
        bestSellerService.updateLast3DaysSalesStat(salesStat);

        // then
        var result = bestSellerService.getTop5Last3DaysSalesStat();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(salesStat.getProductId(), result.get(0).getProductId());
        assertEquals(salesStat.getAmount(), result.get(0).getAmount());
    }

    @Test
    @DisplayName("최근 3일 판매량을 저장했을 때, 기존 저장된 판매량이 있으면 증가한다.")
    void 최근_3일_판매량을_저장시_기존_판매량이_있으면_증가() {
        // given
        var salesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 10)
                .create();
        bestSellerService.updateLast3DaysSalesStat(salesStat);

        // when
        var newSalesStat = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 5)
                .create();
        bestSellerService.updateLast3DaysSalesStat(newSalesStat);

        // then
        var result = bestSellerService.getTop5Last3DaysSalesStat();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(salesStat.getProductId(), result.get(0).getProductId());
        assertEquals(salesStat.getAmount() + newSalesStat.getAmount(), result.get(0).getAmount());
    }

    @Test
    @DisplayName("최근 중 3일 판매량이 가장 많은 상품을 판매량이 많은 순으로 5개 조회한다.")
    void 최근_3일_판매량이_가장_많은_상품을_5개_조회() {
        // given
        var salesStat1 = Instancio.of(SalesStat.class)
                .set(field("productId"), 1L)
                .set(field("amount"), 30)
                .create();
        var salesStat2 = Instancio.of(SalesStat.class)
                .set(field("productId"), 2L)
                .set(field("amount"), 20)
                .create();
        var salesStat3 = Instancio.of(SalesStat.class)
                .set(field("productId"), 3L)
                .set(field("amount"), 10)
                .create();
        var salesStat4 = Instancio.of(SalesStat.class)
                .set(field("productId"), 4L)
                .set(field("amount"), 40)
                .create();
        var salesStat5 = Instancio.of(SalesStat.class)
                .set(field("productId"), 5L)
                .set(field("amount"), 50)
                .create();
        var salesStat6 = Instancio.of(SalesStat.class)
                .set(field("productId"), 6L)
                .set(field("amount"), 60)
                .create();
        bestSellerService.updateLast3DaysSalesStat(salesStat1);
        bestSellerService.updateLast3DaysSalesStat(salesStat2);
        bestSellerService.updateLast3DaysSalesStat(salesStat3);
        bestSellerService.updateLast3DaysSalesStat(salesStat4);
        bestSellerService.updateLast3DaysSalesStat(salesStat5);
        bestSellerService.updateLast3DaysSalesStat(salesStat6);

        // when
        var result = bestSellerService.getTop5Last3DaysSalesStat();

        // then
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(salesStat6.getProductId(), result.get(0).getProductId());
        assertEquals(salesStat5.getProductId(), result.get(1).getProductId());
        assertEquals(salesStat4.getProductId(), result.get(2).getProductId());
        assertEquals(salesStat1.getProductId(), result.get(3).getProductId());
        assertEquals(salesStat2.getProductId(), result.get(4).getProductId());
    }
}