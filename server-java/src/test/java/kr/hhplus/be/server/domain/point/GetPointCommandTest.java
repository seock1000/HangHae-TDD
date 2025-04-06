package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetPointCommandTest {
    @Test
    @DisplayName("GetPointCommand 생성 시 0보다 큰 사용자 ID를 받으면 생성에 성공한다.")
    void GetPointCommand_ValidArgs_Success() {
        // given
        long userId = 1L;

        // when
        GetPointCommand command = new GetPointCommand(userId);

        // then
        assertEquals(userId, command.userId());
    }

    @Test
    @DisplayName("GetPointCommand 생성 시 userId가 0이하이면 IllegalArgumentException 예외가 발생한다.")
    void GetPointCommand_NegativeUserId_ThrowsIllegalArgumentException() {
        // given
        long userId = 0L;

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new GetPointCommand(userId);
        });
        assertEquals("유효하지 않은 사용자 ID 입니다.", exception.getMessage());
    }
}