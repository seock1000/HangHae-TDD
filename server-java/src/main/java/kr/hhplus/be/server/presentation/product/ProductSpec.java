package kr.hhplus.be.server.presentation.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 API", description = "상품 관련 API 입니다.")
public interface ProductSpec {

    @Operation(summary = "상품 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(mediaType = "application/json", examples = {
             @ExampleObject(name = "상품 목록 조회 성공", value = """
                    {
                        "code": 200,
                        "status": "OK",
                        "message": "상품 목록 조회 성공",
                        "data": [
                            {
                                "id": 1,
                                "name": "test",
                                "price": 100,
                                "stock": 10
                            }
                        ]
                    }
                    """)
            }))
    })
    public ResponseEntity<BaseResponse<Page<GetProductResponse>>> getProducts(
            int page,
            int size
    );

}
