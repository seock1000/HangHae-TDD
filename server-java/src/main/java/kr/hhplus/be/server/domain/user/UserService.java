package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ApiException.of(ApiError.USER_NOT_FOUND));
    }
}
