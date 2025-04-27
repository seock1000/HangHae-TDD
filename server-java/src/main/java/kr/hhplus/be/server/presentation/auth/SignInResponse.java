package kr.hhplus.be.server.presentation.auth;

import kr.hhplus.be.server.application.auth.SignInResult;

public record SignInResponse(
        Long userId
) {
    public static SignInResponse of(SignInResult result) {
        return new SignInResponse(result.userId());
    }
}
