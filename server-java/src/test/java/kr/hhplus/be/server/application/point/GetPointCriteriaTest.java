package kr.hhplus.be.server.application.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetPointCriteriaTest {

    @Test
    @DisplayName("형식적으로 유효한 사용자 ID가 주어지면 GetPointCriteria 객체를 생성한다.")
    void testValidGetPointCriteria() {
        // given
        long userId = 1L;

        // when
        GetPointCriteria criteria = new GetPointCriteria(userId);

        // then
        assertEquals(userId, criteria.userId());
    }


    @Test
    @DisplayName("GetPointCriteria 생성 시, 사용자 ID가 0보다 작거나 같으면 IllegalArgumentException이 발생한다.")
    void testInvalidUserId() {
        // given
        long userId = 0L;

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new GetPointCriteria(userId);
        });
        assertEquals("잘못된 사용자 ID 형식입니다.", exception.getMessage());
    }
}