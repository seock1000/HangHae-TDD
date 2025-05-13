package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.domain.bestseller.SalesStat;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.bestseller.BestSellerJpaRepository;
import kr.hhplus.be.server.infrastructure.bestseller.BestSellerRedisRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BestSellerFacadeIntegrationTest {

    @Autowired
    private BestSellerFacade bestSellerFacade;
    @Autowired
    private BestSellerRedisRepository bestSellerRedisRepository;
    @Autowired
    private BestSellerJpaRepository bestSellerJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("BestSeller 통계 테이블에서 많은 순서대로 인기 상품을 5개 가져온다")
    void getTodayBestSellersByDate() {
        // given
        var product1 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 1")
                .create());
        var product2 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 2")
                .create());
        var product3 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 3")
                .create());
        var product4 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 4")
                .create());
        var product5 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 5")
                .create());
        var product6 = productJpaRepository.saveAndFlush(Instancio.of(Product.class)
                .set(field("id"), null)
                .set(field("title"), "Product 6")
                .create());

        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product1.getId())
                .set(field("amount"), 100)
                .create());
        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product2.getId())
                .set(field("amount"), 90)
                .create());
        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product3.getId())
                .set(field("amount"), 80)
                .create());
        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product4.getId())
                .set(field("amount"), 70)
                .create());
        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product5.getId())
                .set(field("amount"), 60)
                .create());
        bestSellerRedisRepository.addLast3DaysSalesStat(Instancio.of(SalesStat.class)
                .set(field("productId"), product6.getId())
                .set(field("amount"), 50)
                .create());

        // when
        var result = bestSellerFacade.getTodayBestSellers();

        // then
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals(product1.getId(), result.get(0).productId());
        assertEquals(product2.getId(), result.get(1).productId());
        assertEquals(product3.getId(), result.get(2).productId());
        assertEquals(product4.getId(), result.get(3).productId());
        assertEquals(product5.getId(), result.get(4).productId());
        assertEquals(product1.getTitle(), result.get(0).title());
        assertEquals(product2.getTitle(), result.get(1).title());
        assertEquals(product3.getTitle(), result.get(2).title());
        assertEquals(product4.getTitle(), result.get(3).title());
        assertEquals(product5.getTitle(), result.get(4).title());
    }
}