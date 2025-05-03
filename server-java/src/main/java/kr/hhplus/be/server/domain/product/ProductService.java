package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 테스트 필요 없을 듯
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * 상품을 ID로 조회하여 반환한다.
     * 상품이 존재하지 않을 경우 ApiException(PRODUCT_NOT_FOUND)을 발생시킨다.
     */
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
    public Product save(Product product) {
        return productRepository.save(product);
    }


}
