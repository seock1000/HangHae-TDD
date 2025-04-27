package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductFacade productFacade;
    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void getProductAll() {
        // when
        var result = productFacade.getProductAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("상품 전체 조회 테스트 - 상품이 존재하는 경우")
    void getProductAllWithProducts() {
        // given
        List<Product> products = List.of(
                Instancio.of(Product.class)
                .set(field("id"), null)
                .create(),
                Instancio.of(Product.class)
                .set(field("id"), null)
                .create());
        productJpaRepository.saveAll(products);

        // when
        var result = productFacade.getProductAll();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }
}