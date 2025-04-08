package kr.hhplus.be.server.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * TC
     * 존재하는 상품들을 리스트로 반환한다.
     */
    public List<Product> getProducts() {
        return productRepository.getProductsAll();
    }
}
