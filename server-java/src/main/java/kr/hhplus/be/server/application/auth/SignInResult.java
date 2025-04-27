package kr.hhplus.be.server.application.auth;

import kr.hhplus.be.server.domain.auth.Auth;

public record SignInResult(
        Long userId
) {
    public static SignInResult of(Auth auth) {
        return new SignInResult(auth.getUserId());
    }
}
