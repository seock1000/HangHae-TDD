package kr.hhplus.be.server.product.controller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.product.controller.out.GetProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    /**
     * 발생 가능 예외 없음
     */
    public ResponseEntity<BaseResponse<List<GetProductResponse>>> getProducts() {
        return ResponseEntity.ok(
                BaseResponse.success(List.of(new GetProductResponse(1L, "test", 100, 10)))
        );
    }
}
