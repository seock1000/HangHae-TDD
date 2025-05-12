package kr.hhplus.be.server.application.auth;

import jakarta.servlet.http.HttpSession;
import kr.hhplus.be.server.IntegrationTestSupport;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class AuthFacadeConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private AuthFacade authFacade;

    @Test
    @DisplayName("동시에 같은 아이디로 가입 시 하나의 요청만 성공해야 한다")
    void 동시에_같은_아이디로_가입시_하나의_요청만_성공해야_한다() {
        // given
        List<SignUpCommand> commands = List.of(
                new SignUpCommand("testUser", "testPwd", new MockHttpSession()),
                new SignUpCommand("testUser", "testPwd", new MockHttpSession()),
                new SignUpCommand("testUser", "testPwd", new MockHttpSession())
        );
        AtomicInteger successCount = new AtomicInteger(0);
        int expectedSuccessCount = 1;

        // when
        List<CompletableFuture<Void>> futures = commands.stream()
                .map(command -> CompletableFuture.runAsync(() -> {
                    try {
                        authFacade.signUp(command);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        //then
        assertEquals(expectedSuccessCount, successCount.get());
    }
}