package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.command.DecreaseStockCommand;
import kr.hhplus.be.server.domain.product.command.IncreaseStockCommand;
import kr.hhplus.be.server.domain.product.error.ProductNotExist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
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

    @Test
    @DisplayName("재고 차감 시, 존재하는 상품이면 재고가 차감된다.")
    void decreaseStock() {
        // given
        long productId = 1L;
        int amount = 10;
        DecreaseStockCommand command = new DecreaseStockCommand(productId, amount);
        Product givenProduct = new Product(productId, "상품1", "상품1_desc", 1000, 100);
        Product expectedProduct = new Product(productId, "상품1", "상품1_desc", 1000, givenProduct.getStock() - amount);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(givenProduct));
        when(productRepository.save(any())).thenReturn(expectedProduct);

        // when
        Product result = productService.decreaseStock(command);

        // then
        verify(productRepository).findById(productId);
        verify(productRepository).save(expectedProduct);
        assertEquals(expectedProduct, result);
    }

    @Test
    @DisplayName("재고 차감 시, 존재하지 않는 상품이면 ProductNotExist 예외가 발생한다.")
    void decreaseStock_ProductNotExist() {
        // given
        long productId = 1L;
        int amount = 10;
        DecreaseStockCommand command = new DecreaseStockCommand(productId, amount);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when, then
        Exception exception = assertThrows(ProductNotExist.class, () -> {
            productService.decreaseStock(command);
        });
        assertEquals("존재하지 않는 상품입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("재고 증가 시, 존재하는 상품이면 재고가 증가한다.")
    void increaseStock() {
        // given
        long productId = 1L;
        int amount = 10;
        IncreaseStockCommand command = new IncreaseStockCommand(productId, amount);
        Product givenProduct = new Product(productId, "상품1", "상품1_desc", 1000, 100);
        Product expectedProduct = new Product(productId, "상품1", "상품1_desc", 1000, givenProduct.getStock() + amount);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(givenProduct));
        when(productRepository.save(any())).thenReturn(expectedProduct);

        // when
        Product result = productService.increaseStock(command);

        // then
        verify(productRepository).findById(productId);
        verify(productRepository).save(expectedProduct);
        assertEquals(expectedProduct, result);
    }

    @Test
    @DisplayName("재고 증가 시, 존재하지 않는 상품이면 ProductNotExist 예외가 발생한다.")
    void increaseStock_ProductNotExist() {
        // given
        long productId = 1L;
        int amount = 10;
        IncreaseStockCommand command = new IncreaseStockCommand(productId, amount);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when, then
        Exception exception = assertThrows(ProductNotExist.class, () -> {
            productService.increaseStock(command);
        });
        assertEquals("존재하지 않는 상품입니다.", exception.getMessage());
    }
}