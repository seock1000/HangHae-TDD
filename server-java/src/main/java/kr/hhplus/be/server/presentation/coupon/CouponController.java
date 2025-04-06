package kr.hhplus.be.server.presentation.coupon;

import kr.hhplus.be.server.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController implements CouponSpec {

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * */
    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetCouponsResponse>>> getCoupons(
            @RequestParam("userId") Long userId
    ) {
        if(userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body(
                    BaseResponse.fail(BAD_REQUEST, "유효하지 않은 사용자입니다."));
        }
        return ResponseEntity.ok(
                BaseResponse.success(
                        List.of(new GetCouponsResponse(1L, "test", "RATE", 10, LocalDate.now(), LocalDate.now()))
                )
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
        if(request.userId() == null || request.userId() <= 0) {
            return ResponseEntity.badRequest().body(
                    BaseResponse.fail(BAD_REQUEST, "유효하지 않은 사용자입니다."));
        }
        if(request.couponId() == null || request.couponId() <= 0) {
            return ResponseEntity.badRequest().body(
                    BaseResponse.fail(BAD_REQUEST, "유효하지 않은 쿠폰입니다."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created()
        );
    }
}
