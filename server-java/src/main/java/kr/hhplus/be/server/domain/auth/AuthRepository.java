package kr.hhplus.be.server.domain.auth;

import java.util.Optional;

public interface AuthRepository {

    Optional<Auth> findByUsername(String username);

    boolean existsByUsername(String username);

    Auth save(Auth auth);
}
