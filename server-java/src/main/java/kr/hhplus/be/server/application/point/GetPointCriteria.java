package kr.hhplus.be.server.application.point;

/**
 * TC
 * 사용자 ID가 0보다 작거나 같으면 실패 => IllegalArgumentException, "잘못된 사용자 ID 형식입니다."
 */
public record GetPointCriteria(
        long userId
) {
    public GetPointCriteria {
        if (userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID 형식입니다.");
        }
    }
}
