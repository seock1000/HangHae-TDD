package kr.hhplus.be.server.presentation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthSpec {

    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "회원가입 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "회원가입 성공",
                                "data": [
                                    {
                                        "userId": 1
                                    }
                                ]
                            }
                            """)
            }))
    })
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(
            @RequestBody SignUpRequest request,
            HttpServletRequest HttpRequest
    );

    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "로그인 성공", value = """
                            {
                                "code": 200,
                                "status": "OK",
                                "message": "로그인 성공",
                                "data": [
                                    {
                                        "userId": 1
                                    }
                                ]
                            }
                            """)
            }))
    })
    public ResponseEntity<BaseResponse<SignInResponse>> signIn(
            @RequestBody SignInRequest request,
            HttpServletRequest HttpRequest
    );
}
