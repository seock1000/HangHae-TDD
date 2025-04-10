package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.error.InvalidUserIdError;

public record GetUserPointCommand(
        long userId
) {
    public GetUserPointCommand {
        if (userId <= 0) {
            throw InvalidUserIdError.of("잘못된 사용자 ID 형식입니다.");
        }
    }
}
