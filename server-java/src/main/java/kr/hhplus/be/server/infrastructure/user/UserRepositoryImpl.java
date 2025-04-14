package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {
    private final UserJpaRepository userJpaRepository;

    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }
}
