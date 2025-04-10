package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetUserCommandTest {

    @Test
    @DisplayName("GetUserCommand 생성 시 userId가 0 보다 크면 정상적으로 생성된다.")
    void testValidUserId() {
        // given
        long userId = 1L;
        // when
        GetUserCommand command = new GetUserCommand(userId);
        // then
        assertEquals(userId, command.userId());
    }

    @Test
    @DisplayName("GetUserCommand 생성 시 userId가 0 이하이면 InvalidUserIdError가 발생한다.")
    void testInvalidUserId() {
        // given
        long userId = 0L;
        // when
        Exception exception = assertThrows(InvalidUserIdError.class, () -> new GetUserCommand(userId));
        // then
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }

}