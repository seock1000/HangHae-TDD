package kr.hhplus.be.server.presentation.auth;

import jakarta.servlet.http.HttpSession;
import kr.hhplus.be.server.application.auth.SignInCommand;

public record SignInRequest(
        String username,
        String password
) {
    public SignInRequest {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }

    public SignInCommand toCommand(HttpSession session) {
        return new SignInCommand(username, password, session);
    }
}
