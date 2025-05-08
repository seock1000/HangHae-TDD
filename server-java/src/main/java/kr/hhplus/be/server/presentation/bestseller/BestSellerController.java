package kr.hhplus.be.server.presentation.bestseller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.bestseller.BestSellerFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/best-sellers")
@RequiredArgsConstructor
public class BestSellerController implements BestSellerSpec {

    private final BestSellerFacade bestSellerFacade;

    /**
     * 발생 가능 예외 없음
     */
    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetBestSellerResponse>>> getBestSellers() {

        List<GetBestSellerResponse> responses = bestSellerFacade.getTodayBestSellers().stream()
                .map(GetBestSellerResponse::of)
                .toList();

        return ResponseEntity.ok(
                BaseResponse.success(responses)
        );
    }

}
