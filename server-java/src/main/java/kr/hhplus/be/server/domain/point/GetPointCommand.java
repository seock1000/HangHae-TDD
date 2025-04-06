package kr.hhplus.be.server.domain.point;

public record GetPointCommand(
        long userId
) {
    public GetPointCommand {
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.");
        }
    }
}
