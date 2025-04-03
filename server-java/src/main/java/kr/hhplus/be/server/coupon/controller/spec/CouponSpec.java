package kr.hhplus.be.server.coupon.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.coupon.controller.in.IssueCouponRequest;
import kr.hhplus.be.server.coupon.controller.out.GetCouponsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "쿠폰 API", description = "쿠폰 관련 API 입니다.")
public interface CouponSpec {

    @Operation(summary = "쿠폰 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쿠폰 조회 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "쿠폰 조회 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "쿠폰 조회 성공",
                                "data": [
                                    {
                                        "id": 1,
                                        "title": "test",
                                        "discountType": "RATE",
                                        "discountValue": 10,
                                        "startDate": "2023-01-01",
                                        "endDate": "2023-12-31"
                                    }
                                ]
                            }
                            """)
            })),
    })
    ResponseEntity<BaseResponse<List<GetCouponsResponse>>> getCoupons(
            @RequestParam("userId") Long userId
    );


    @Operation(summary = "쿠폰 발급")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "쿠폰 발급 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "쿠폰 발급 성공", value = """
                            {
                                "code": 201,
                                "status": "CREATED",
                                "message": "쿠폰 발급 성공"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 사용자입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 사용자", value = """
                            {
                                "code": 400,
                                "status": "BAD_REQUEST",
                                "message": "유효하지 않은 사용자입니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 쿠폰입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 쿠폰", value = """
                            {
                                "code": 400,
                                "status": "BAD_REQUEST",
                                "message": "유효하지 않은 쿠폰입니다."
                            }
                            """)
            }))
    })
    ResponseEntity<BaseResponse<Void>> issueCoupon(
            @RequestBody IssueCouponRequest request
    );
}
