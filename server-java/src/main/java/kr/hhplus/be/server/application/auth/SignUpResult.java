package kr.hhplus.be.server.application.auth;

import kr.hhplus.be.server.domain.auth.Auth;

public record SignUpResult(
        Long userId
) {
    public static SignUpResult of(Auth auth) {
        return new SignUpResult(auth.getUserId());
    }
}
