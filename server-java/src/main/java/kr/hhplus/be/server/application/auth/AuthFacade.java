package kr.hhplus.be.server.application.auth;

import kr.hhplus.be.server.config.redis.DistributedLock;
import kr.hhplus.be.server.domain.auth.Auth;
import kr.hhplus.be.server.domain.auth.AuthService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthFacade {

    private final AuthService authService;
    private final UserService userService;

    @DistributedLock(key = "'auth:' + #command.username()")
    public SignUpResult signUp(SignUpCommand command) {
        User signedUpUser = userService.createUser();
        Auth auth = authService.createWithUser(command.username(), command.password(), signedUpUser);
        auth.registerSession(command.session());

        return SignUpResult.of(auth);
    }

    public SignInResult signIn(SignInCommand command) {
        Auth auth = authService.signIn(command.username(), command.password());
        auth.registerSession(command.session());

        return SignInResult.of(auth);
    }
}
