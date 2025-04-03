package kr.hhplus.be.server.order.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.order.controller.in.CreateOrderRequest;
import kr.hhplus.be.server.order.controller.out.CreateOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "주문 API", description = "주문 API 입니다.")
public interface OrderSpec {

    @Operation(summary = "주문 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "주문 생성 성공", value = """
                            {
                                "code": 201,
                                "status": "CREATED",
                                "message": "주문 생성 성공",
                                "data": {
                                    "orderId": 1
                                }
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
            @ApiResponse(responseCode = "400", description = "유효하지 않은 상품입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 상품", value = """
                            {
                                "code": 400,
                                "status": "BAD_REQUEST",
                                "message": "유효하지 않은 상품입니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 상품 수량입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 상품 수량", value = """
                            {
                                "code": 400,
                                "status": "BAD_REQUEST",
                                "message": "유효하지 않은 상품 수량입니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "409", description = "유효하지 않은 쿠폰입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 쿠폰", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "유효하지 않은 쿠폰입니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "409", description = "재고가 부족합니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "재고 부족", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "재고가 부족합니다."
                            }
                            """)
            }))
    })
    ResponseEntity<BaseResponse<CreateOrderResponse>> getBestSellers(
            @RequestBody CreateOrderRequest request
    );
}
