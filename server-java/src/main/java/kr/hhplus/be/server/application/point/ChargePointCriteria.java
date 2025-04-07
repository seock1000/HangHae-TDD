package kr.hhplus.be.server.application.point;

/**
 * TC
 * 사용자 ID가 0보다 작거나 같으면 실패 => IllegalArgumentException, "잘못된 사용자 ID 형식입니다."
 * 충전 금액이 0보다 작거나 같으면 실패 => IllegalArgumentException, "잘못된 충전 금액 형식입니다."
 */
public record ChargePointCriteria(
        long userId,
        int amount
) {
    public ChargePointCriteria {
        if (userId <= 0) {
            throw new IllegalArgumentException("잘못된 사용자 ID 형식입니다.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("잘못된 충전 금액 형식입니다.");
        }
    }
}
