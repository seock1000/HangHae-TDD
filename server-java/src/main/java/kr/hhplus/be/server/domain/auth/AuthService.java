package kr.hhplus.be.server.domain.auth;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;

    public Auth createWithUser(String username, String password, User user) {
        if(authRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        return authRepository.save(Auth.createWithUser(username, password, user));
    }

    public Auth signIn(String username, String password) {
        Auth auth = authRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        if (!auth.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return auth;
    }
}
