package kr.hhplus.be.server.domain.point;

public record ChargePointCommand(
        long userId,
        int amount
) {
    public ChargePointCommand {
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID 입니다.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("유효하지 않은 충전 금액입니다.");
        }
    }
}
