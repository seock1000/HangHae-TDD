package kr.hhplus.be.server.presentation.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "포인트 API", description = "포인트 관련 API 입니다.")
public interface PointSpec {

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 409 : 1회 최대 충전 금액 초과
     * 409 : 최대 잔고 초과
     */
    @Operation(summary = "포인트 충전")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 충전 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "포인트 충전 성공", value = """
                            {
                                "code": 200,
                                "status: "OK",
                                "message": "포인트 충전 성공",
                                "data": {
                                    "userId": 1,
                                    "balance": 10000
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
            @ApiResponse(responseCode = "409", description = "1회 최대 충전 금액 초과", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "1회 최대 충전 금액 초과", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "1회 최대 충전 금액을 초과했습니다."
                            }
                            """)
            })),
            @ApiResponse(responseCode = "409", description = "최대 잔고 초과", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "최대 잔고 초과", value = """
                            {
                                "code": 409,
                                "status": "CONFLICT",
                                "message": "최대 잔고를 초과했습니다."
                            }
                            """)
            }))
    })
    public ResponseEntity<BaseResponse<ChargePointResponse>> charge(ChargePointRequest request);

    /**
     * 발생 가능 예외 없음
     */
    @Operation(summary = "포인트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "포인트 조회 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "포인트 조회 성공",
                                "data": {
                                        "userId": 1,
                                        "balance": 10000
                                        }
                            """)
            }))
    })
    public ResponseEntity<BaseResponse<GetPointResponse>> getPoint(
            @Schema(description = "사용자 ID") Long userId
    );

}
