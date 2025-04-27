package kr.hhplus.be.server.presentation.product;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.product.ProductFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductSpec {

    private final ProductFacade productFacade;

    /**
     * 발생 가능 예외 없음
     */
    @GetMapping
    @Override
    public ResponseEntity<BaseResponse<List<GetProductResponse>>> getProducts() {
        List<GetProductResponse> responses = productFacade.getProductAll()
                .stream()
                .map(GetProductResponse::of)
                .toList();

        return ResponseEntity.ok(
                BaseResponse.success(responses)
        );
    }
}
