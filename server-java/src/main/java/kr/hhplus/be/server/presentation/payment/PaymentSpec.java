package kr.hhplus.be.server.presentation.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "결제 API", description = "결제 관련 API 입니다.")
public interface PaymentSpec {

    @Operation(summary = "결제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "결제 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "결제 성공"
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 주문입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "유효하지 않은 주문", value = """
                            {
                                "code": 400,
                                "status": "BAD_REQUEST",
                                "message": "유효하지 않은 주문입니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "409", description = "잔여 포인트가 부족합니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "잔여 포인트 부족", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "잔여 포인트가 부족합니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "409", description = "취소된 주문입니다.", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "취소된 주문", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "취소된 주문입니다."
                            }
                            """)
            }))
    })
    public ResponseEntity<BaseResponse<Void>> pay(
            PayRequest request
    );
}
