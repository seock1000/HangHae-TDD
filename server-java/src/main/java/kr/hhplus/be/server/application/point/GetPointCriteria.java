package kr.hhplus.be.server.application.point;

public record GetPointCriteria(
        long userId
) {
    public GetPointCriteria {
        if (userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID 형식입니다.");
        }
    }
}
