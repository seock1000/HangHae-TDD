package kr.hhplus.be.server.bestseller.controller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.bestseller.controller.out.GetBestSellerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/best-sellers")
public class BestSellerController {

    /**
     * 발생 가능 예외 없음
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetBestSellerResponse>>> getBestSellers() {
        return ResponseEntity.ok(
                BaseResponse.success(List.of(new GetBestSellerResponse(1L, "test", 100, 200, 100)))
        );
    }

}
