package kr.hhplus.be.server.infrastructure.auth;

import kr.hhplus.be.server.domain.auth.Auth;
import kr.hhplus.be.server.domain.auth.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthJpaRepository authJpaRepository;

    @Override
    public Optional<Auth> findByUsername(String username) {
        return authJpaRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return authJpaRepository.existsByUsername(username);
    }

    @Override
    public Auth save(Auth auth) {
        return authJpaRepository.save(auth);
    }
}
