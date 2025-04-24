package kr.hhplus.be.server;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(ApiException.class)
    protected ResponseEntity<BaseResponse<ApiError>> handleApiException(ApiException e) {
        ApiError apiError = e.getApiError();
        e.printStackTrace();
        return ResponseEntity.status(apiError.getStatus())
                .body(BaseResponse.fail(apiError.getStatus(), apiError.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(ApiError.INTERNAL_SERVER_ERROR.getStatus())
                .body(BaseResponse.fail(ApiError.INTERNAL_SERVER_ERROR.getStatus(), ApiError.INTERNAL_SERVER_ERROR.getMessage()));
    }
}
