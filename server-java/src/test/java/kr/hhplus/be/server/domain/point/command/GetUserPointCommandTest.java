package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.error.InvalidUserIdError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetUserPointCommandTest {

    @Test
    @DisplayName("GetUserPointCommand 생성 시, 사용자 ID가 0보다 크면 생성에 성공한다.")
    void testValidUserId() {
        // given
        long userId = 1L;

        // when
        GetUserPointCommand command = new GetUserPointCommand(userId);

        // then
        assertEquals(userId, command.userId());
    }

    @Test
    @DisplayName("GetUserPointCommand 생성 시, 사용자 ID가 0보다 작거나 같으면 InvalidUserIdError가 발생한다.")
    void testInvalidUserId() {
        // given
        long userId = 0L;

        // when
        Exception exception = assertThrows(InvalidUserIdError.class, () -> new GetUserPointCommand(userId));

        // then
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }


}