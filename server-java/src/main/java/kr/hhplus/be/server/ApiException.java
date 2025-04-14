package kr.hhplus.be.server;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final ApiError apiError;

    private ApiException(ApiError apiError) {
        super(apiError.getMessage());
        this.apiError = apiError;
    }

    public static ApiException of(ApiError apiError) {
        return new ApiException(apiError);
    }
}
