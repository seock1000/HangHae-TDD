package kr.hhplus.be.server.application.point;

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
