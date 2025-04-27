package kr.hhplus.be.server.infrastructure.auth;

import kr.hhplus.be.server.domain.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthJpaRepository extends JpaRepository<Auth, Long> {
    boolean existsByUsername(String username);

    Optional<Auth> findByUsername(String username);
}
