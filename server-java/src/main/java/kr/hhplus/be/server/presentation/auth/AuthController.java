package kr.hhplus.be.server.presentation.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.auth.AuthFacade;
import kr.hhplus.be.server.application.auth.SignInResult;
import kr.hhplus.be.server.application.auth.SignUpResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<SignUpResponse>> signUp(
            @RequestBody SignUpRequest request,
            HttpServletRequest HttpRequest
    ) {
        HttpSession session = HttpRequest.getSession();
        SignUpResult result = authFacade.signUp(request.toCommand(session));

        return ResponseEntity.ok(BaseResponse.success(SignUpResponse.of(result)));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponse<SignInResponse>> signIn(
            @RequestBody SignInRequest request,
            HttpServletRequest HttpRequest
    ) {
        HttpSession session = HttpRequest.getSession();
        SignInResult result = authFacade.signIn(request.toCommand(session));

        return ResponseEntity.ok(BaseResponse.success(SignInResponse.of(result)));
    }
}
