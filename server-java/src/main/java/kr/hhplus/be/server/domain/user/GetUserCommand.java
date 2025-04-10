package kr.hhplus.be.server.domain.user;

public record GetUserCommand(
        long userId
) {
    public GetUserCommand {
        if (userId <= 0) {
            throw InvalidUserIdError.of("잘못된 사용자 ID 형식입니다.");
        }
    }
}
