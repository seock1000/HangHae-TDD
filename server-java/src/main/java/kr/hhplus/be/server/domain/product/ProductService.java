package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.config.cache.CacheKey;
import kr.hhplus.be.server.config.cache.CacheManagerName;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 테스트 필요 없을 듯
     */
    @Cacheable(value = CacheKey.PRODUCTS,
            cacheManager = CacheManagerName.GLOBAL,
            key = CacheKey.PRODUCTS_EL + " + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * 상품을 ID로 조회하여 반환한다.
     * 상품이 존재하지 않을 경우 ApiException(PRODUCT_NOT_FOUND)을 발생시킨다.
     */
    @Cacheable(value = CacheKey.PRODUCT,
            cacheManager = CacheManagerName.GLOBAL,
            key = CacheKey.PRODUCT_EL + " + ':' + #id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> ApiException.of(ApiError.PRODUCT_NOT_FOUND));
    }

    public Product getProductByIdForUpdate(Long id) {
        return productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> ApiException.of(ApiError.PRODUCT_NOT_FOUND));
    }

    /**
     * 테스트 필요 없을 듯
     */
    @Caching(evict = {
            @CacheEvict(value = CacheKey.PRODUCTS,
                    cacheManager = CacheManagerName.GLOBAL,
                    allEntries = true)},
            put = {
            @CachePut(value = CacheKey.PRODUCT,
                    cacheManager = CacheManagerName.GLOBAL,
                    key = CacheKey.PRODUCT_EL + " + ':' + #result.id")}
    )
    public Product save(Product product) {
        return productRepository.save(product);
    }


}
