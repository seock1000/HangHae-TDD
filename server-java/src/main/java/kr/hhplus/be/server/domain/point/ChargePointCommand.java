package kr.hhplus.be.server.domain.point;

public record ChargePointCommand(
        Long userId,
        Integer amount
) {
}
