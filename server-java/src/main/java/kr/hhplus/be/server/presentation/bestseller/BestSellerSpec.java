package kr.hhplus.be.server.presentation.bestseller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "베스트셀러 API", description = "베스트셀러 관련 API 입니다.")
public interface BestSellerSpec {

    @Operation(summary = "베스트셀러 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "베스트셀러 조회 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "베스트셀러 조회 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "베스트셀러 조회 성공",
                                "data": [
                                    {
                                        "id": 1,
                                        "name": "test",
                                        "price": 100,
                                        "sales": 200,
                                        "stock": 100
                                    }
                                ]
                            }
                            """)
            })),
    })
    ResponseEntity<BaseResponse<List<GetBestSellerResponse>>> getBestSellers();
}
