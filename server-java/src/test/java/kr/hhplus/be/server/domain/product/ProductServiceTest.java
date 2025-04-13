package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 ID로 조회하여 반환한다.")
    void getProductById() {
        // given
        Long productId = 1L;
        Product product = Instancio.of(Product.class)
                .set(field("id"), productId)
                .create();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        Product result = productService.getProductById(productId);

        // then
        assertEquals(product, result);
    }

    @Test
    @DisplayName("상품이 존재하지 않을 경우 ApiException(PRODUCT_NOT_FOUND)을 발생시킨다.")
    void getProductById_ProductNotFound() {
        // given
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> productService.getProductById(productId));
        assertEquals(ApiError.PRODUCT_NOT_FOUND, exception.getApiError());
    }

}