package kr.hhplus.be.server.domain.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.config.cache.CacheKey;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceCacheTest {

    @Autowired
    private ProductService productService;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void tearDown() {
        cacheManager.getCache(CacheKey.PRODUCT).clear();
        cacheManager.getCache(CacheKey.PRODUCTS).clear();
    }

    @Test
    @DisplayName("상품 Page를 조회하면 캐시에 저장된다.")
    void 상품_페이지_조회시_캐시_저장() {
        // given
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);
        for (int i = 0; i < pageSize; i++) {
            productService.save(Instancio.of(Product.class)
                    .set(field("id"), null)
                    .create());
        }

        // when
        Page<Product> result = productService.getAllProducts(pageable);

        // then
        Cache cache = cacheManager.getCache(CacheKey.PRODUCTS);
        assertNotNull(cache);
        // 캐시에서 값을 가져와서 확인
        TypeReference<PageImpl<Product>> typeRef = new TypeReference<>() {};

        PageImpl<Product> cacheValue = objectMapper.convertValue(
                cache.get(CacheKey.PRODUCTS + ":" + pageable.getPageNumber() + ":" + pageable.getPageSize()).get(),
                typeRef
        );
        assertNotNull(cacheValue);
        assertEquals(result.getContent().size(), cacheValue.getContent().size());
        assertIterableEquals(
                result.getContent().stream().map(Product::getId).sorted().toList(),
                cacheValue.getContent().stream().map(Product::getId).sorted().toList()
        );
    }

    @Test
    @DisplayName("상품 ID로 조회하면 캐시에 저장된다.")
    void 상품_ID로_조회시_캐시_저장() {
        // given
        Product product = productService.save(Instancio.of(Product.class)
                .set(field("id"), null)
                .create());
        System.out.println(product.getId());

        // when
        Product result = productService.getProductById(product.getId());

        // then
        Cache cache = cacheManager.getCache(CacheKey.PRODUCT);
        Product cacheValue = cache.get(CacheKey.PRODUCT + ":" + result.getId(), Product.class);
        assertNotNull(cache);
        assertNotNull(cacheValue);
        assertEquals(result.getId(), cacheValue.getId());
    }

    @Test
    @DisplayName("상품 정보 저장 시, 상품 목록 캐시는 삭제하고 저장된 상품의 캐시를 갱신한다")
    void 상품_정보_저장시_캐시_갱신() {
        // given
        Integer givenStock = 100;
        Product dummyProduct = productService.save(
                Instancio.of(Product.class)
                        .set(field("id"), null)
                        .set(field("stock"), givenStock)
                        .create()
        );
        Page<Product> dummyPage = new PageImpl<>(
                List.of(dummyProduct),
                PageRequest.of(0, 10),
                1
        );
        String productKey = CacheKey.PRODUCT + ":" + dummyProduct.getId();
        String productsKey = CacheKey.PRODUCTS + ":" + dummyPage.getPageable().getPageNumber() + ":" + dummyPage.getPageable().getPageSize();

        Objects.requireNonNull(cacheManager.getCache(CacheKey.PRODUCT))
                .put(productKey, dummyProduct);
        Objects.requireNonNull(cacheManager.getCache(CacheKey.PRODUCTS))
                .put(productsKey, dummyPage);

        // when
        dummyProduct.decreaseStock(1);
        productService.save(dummyProduct);

        // then
        Cache productCache = cacheManager.getCache(CacheKey.PRODUCT);
        assertNotNull(productCache);
        Product cacheValue = productCache.get(productKey, Product.class);
        assertNotNull(cacheValue);
        assertEquals(dummyProduct.getId(), cacheValue.getId());
        assertEquals(dummyProduct.getStock(), cacheValue.getStock()); // 재고가 감소했는지 확인

        Cache productsCache = cacheManager.getCache(CacheKey.PRODUCTS);
        assertNotNull(productsCache);
        assertNull(productsCache.get(productsKey, Page.class)); // 상품 목록 캐시는 삭제되었는지 확인
    }
}