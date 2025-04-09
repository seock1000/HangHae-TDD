package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.command.DecreaseStockCommand;
import kr.hhplus.be.server.domain.product.command.IncreaseStockCommand;
import kr.hhplus.be.server.domain.product.error.ProductNotExist;
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

    /**
     * 재고 차감
     * 상품을 조회하여 재고를 차감하고 저장한다.
     * TC
     * 상품이 존재하지 않으면 ProductNotExist 발생시킨다.
     */
    public Product decreaseStock(DecreaseStockCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> ProductNotExist.of("존재하지 않는 상품입니다."));

        product.decreaseStock(command.amount());
        return productRepository.save(product);
    }

    /**
     * 재고 증가
     * 상품을 조회하여 재고를 증가시키고 저장한다.
     * TC
     * 상품이 존재하지 않으면 InvalidProductError를 발생시킨다.
     */
    public Product increaseStock(IncreaseStockCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> ProductNotExist.of("존재하지 않는 상품입니다."));

        product.increaseStock(command.amount());
        return productRepository.save(product);
    }


}
