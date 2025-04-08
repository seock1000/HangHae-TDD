package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("존재하는 상품들을 리스트로 조회한다.")
    void getProducts() {
        // given
        Product product1 = new Product(1L, "상품1", "상품1_desc", 1000, 100);
        Product product2 = new Product(2L, "상품2", "상품2_desc", 2000, 200);
        List<Product> givenProducts = List.of(product1, product2);

        when(productRepository.getProductsAll()).thenReturn(givenProducts);

        // when
        List<Product> result = productService.getProducts();

        // then
        assertIterableEquals(givenProducts, result);
    }

}