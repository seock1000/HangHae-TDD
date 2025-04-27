package kr.hhplus.be.server.presentation.auth;

import kr.hhplus.be.server.application.auth.SignUpResult;

public record SignUpResponse(
        Long userId
) {
    public static SignUpResponse of(SignUpResult result) {
        return new SignUpResponse(result.userId());
    }
}
