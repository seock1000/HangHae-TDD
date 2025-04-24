package kr.hhplus.be.server.presentation.auth;

import jakarta.servlet.http.HttpSession;
import kr.hhplus.be.server.application.auth.SignUpCommand;

public record SignUpRequest(
        String username,
        String password
) {
    public SignUpRequest {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }

    public SignUpCommand toCommand(HttpSession session) {
        return new SignUpCommand(username, password, session);
    }
}
