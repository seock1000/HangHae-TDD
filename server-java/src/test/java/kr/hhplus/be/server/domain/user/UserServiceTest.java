package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 조회 시 유저가 존재하면 정상적으로 조회된다.")
    void getUserById_UserExists_ReturnsUser() {
        // given
        Long userId = 1L;
        User user = Instancio.of(User.class)
                .set(field("id"), userId)
                .create();

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // then
        User result = userService.getUserById(userId);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("유저 조회 시 유저가 존재하지 않으면 ApiException(USER_NOT_FOUND)이 발생한다.")
    void getUserById_UserNotFound_ThrowsApiException() {
        // given
        Long userId = 1L;

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // then
        ApiException exception = assertThrows(ApiException.class, () -> userService.getUserById(userId));
        assertEquals(ApiError.USER_NOT_FOUND, exception.getApiError());
    }
}