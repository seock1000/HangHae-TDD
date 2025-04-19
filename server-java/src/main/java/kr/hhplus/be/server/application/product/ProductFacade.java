package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductFacade {

    private final ProductService productService;

    /**
     * 테스트 필요 없을 듯
     */
    @Transactional(readOnly = true)
    public List<ProductResult> getProductAll() {
        return productService.getAllProducts()
                .stream()
                .map(ProductResult::of)
                .toList();
    }

}
