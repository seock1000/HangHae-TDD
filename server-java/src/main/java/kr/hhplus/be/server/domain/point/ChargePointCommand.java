package kr.hhplus.be.server.domain.point;

public record ChargePointCommand(
        long userId,
        int amount
) {
}
