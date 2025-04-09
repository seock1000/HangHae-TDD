package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    public List<Product> getProductsAll();

    public Optional<Product> findById(Long id);
}
