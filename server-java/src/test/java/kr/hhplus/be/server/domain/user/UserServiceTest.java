package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자 조회 시 사용자가 존재하면 정상적으로 사용자 정보를 가져온다.")
    void testGetUser() {
        // given
        GetUserCommand command = new GetUserCommand(1L);
        when(userRepository.findById(command.userId())).thenReturn(Optional.of(new User(command.userId())));

        // when
        User user = userService.getUserById(command);

        // then
        assertNotNull(user);
        assertEquals(command.userId(), user.getId());
    }

    @Test
    @DisplayName("사용자 조회시 사용자가 존재하지 않으면 UserNotExistError가 발생한다.")
    void testGetUserNotExist() {
        // given
        GetUserCommand command = new GetUserCommand(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(UserNotExistError.class, () -> userService.getUserById(command));
        assertEquals("사용자가 존재하지 않습니다.", exception.getMessage());
    }

}