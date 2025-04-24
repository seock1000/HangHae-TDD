package kr.hhplus.be.server.domain.auth;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Auth extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Long userId;


    private Auth(String username, String password, Long userId) {
        this.username = username;
        this.password = password;
        this.userId = userId;
    }

    public static Auth createWithUser(String username, String password, User user) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return new Auth(username, password, user.getId());
    }

    public void registerSession(HttpSession session) {
        session.setAttribute("ecommerce-user-id", this.userId);
        session.setMaxInactiveInterval(60 * 60); // 1 hour
    }

}
