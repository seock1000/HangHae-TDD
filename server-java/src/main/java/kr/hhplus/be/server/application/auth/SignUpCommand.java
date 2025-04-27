package kr.hhplus.be.server.application.auth;

import jakarta.servlet.http.HttpSession;

public record SignUpCommand(
        String username,
        String password,
        HttpSession session
) {
    public SignUpCommand {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }
    }
}
