package kr.hhplus.be.server.application.bestseller;

import kr.hhplus.be.server.domain.bestseller.BestSeller;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.bestseller.BestSellerBaseJpaRepository;
import kr.hhplus.be.server.infrastructure.bestseller.BestSellerJpaRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BestSellerScheduledFacadeIntegrationTest {

    @Autowired
    private BestSellerScheduledFacade bestSellerScheduledFacade;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private BestSellerJpaRepository bestSellerJpaRepository;
    @Autowired
    private BestSellerBaseJpaRepository bestSellerBaseJpaRepository;

    @Test
    @DisplayName("어제의 판매량을 가져와 best_seller_base 테이블에 저장하고, 어제의 판매량을 기준으로 3일치 판매량을 합해 best_seller 테이블에 저장한다.")
    void saveYesterdayBestSellers() {
        // given
        var yesterday = LocalDate.now().minusDays(1);
        var orderParams = List.of(
                Instancio.of(Orders.class)
                        .set(field("id"), "orderId1")
                        .set(field("orderDate"), yesterday)
                        .set(field("status"), OrderStatus.CONFIRMED)
                        .set(field("orderItems"), new ArrayList<>())
                        .create(),
                Instancio.of(Orders.class)
                        .set(field("id"), "orderId2")
                        .set(field("orderDate"), yesterday)
                        .set(field("status"), OrderStatus.CONFIRMED)
                        .set(field("orderItems"), new ArrayList<>())
                        .create(),
                Instancio.of(Orders.class)
                        .set(field("id"), "orderId3")
                        .set(field("orderDate"), yesterday.minusDays(1))
                        .set(field("status"), OrderStatus.CONFIRMED)
                        .set(field("orderItems"), new ArrayList<>())
                        .create(),
                Instancio.of(Orders.class)
                        .set(field("id"), "orderId4")
                        .set(field("orderDate"), yesterday.minusDays(2))
                        .set(field("status"), OrderStatus.CONFIRMED)
                        .set(field("orderItems"), new ArrayList<>())
                        .create()
        );
        orderJpaRepository.saveAllAndFlush(orderParams);

        var product1 = Instancio.of(Product.class)
                .set(field("id"), 1L)
                .set(field("stock"), 100)
                .create()
                .toSoldProduct();
        var product2 = Instancio.of(Product.class)
                .set(field("id"), 2L)
                .set(field("stock"), 50)
                .create()
                .toSoldProduct();

        orderParams.get(0).addProduct(product1, 10);
        orderParams.get(0).addProduct(product2, 5);
        orderParams.get(1).addProduct(product1, 20);
        orderParams.get(1).addProduct(product2, 10);
        orderParams.get(2).addProduct(product1, 10);
        orderParams.get(2).addProduct(product2, 5);
        orderParams.get(3).addProduct(product1, 10);
        orderParams.get(3).addProduct(product2, 5);

        orderJpaRepository.saveAllAndFlush(orderParams);

        // when&then
        bestSellerScheduledFacade.saveYesterdaySalesAmounts();
        assertEquals(2, bestSellerBaseJpaRepository.findAll().size());
        List<BestSeller> bestSellers = bestSellerJpaRepository.findAll();
        assertEquals(2, bestSellers.size());
        assertEquals(1L, bestSellers.get(0).getProductId());
        assertEquals(2L, bestSellers.get(1).getProductId());
        assertEquals(30, bestSellers.get(0).getSalesAmount());
        assertEquals(15, bestSellers.get(1).getSalesAmount());
    }
}