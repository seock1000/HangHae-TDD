package kr.hhplus.be.server.presentation.coupon;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.GetUserCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController implements CouponSpec {

    private final CouponFacade couponFacade;

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * */
    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetCouponsResponse>>> getCoupons(
            @RequestParam("user-id") Long userId
    ) {
        List<GetCouponsResponse> responses = couponFacade.getUserCouponInfosByUser(new GetUserCouponCommand(userId))
                .stream()
                .map(GetCouponsResponse::of)
                .toList();

        return ResponseEntity.ok(
                BaseResponse.success(responses)
        );
    }

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 400 : 유효하지 않은 쿠폰입니다.
     */
    @Override
    @PostMapping("/issue")
    public ResponseEntity<BaseResponse<Void>> issueCoupon(
                @RequestBody IssueCouponRequest request
    ) {
        couponFacade.issueCoupon(request.toCommand());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created()
        );
    }
}
