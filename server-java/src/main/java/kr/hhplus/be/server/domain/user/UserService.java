package kr.hhplus.be.server.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(GetUserCommand command) {
        return userRepository.findById(command.userId())
                .orElseThrow(() -> UserNotExistError.of("사용자가 존재하지 않습니다."));
    }
}
