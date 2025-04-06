package kr.hhplus.be.server.domain.point;

public interface PointService {
    /**
     * TC
     * 존재하지 않는 유저이면 실패 => IllegalArgumentException("존재하지 않는 유저입니다.")
     * 충전금액이 0 이하이면 실패 => IllegalArgumentException("충전금액은 0보다 커야합니다.")
     * 1회 최대 충전 가능 금액 1_000_000 초과시 실패 => IllegalArgumentException("1회 최대 충전 가능 금액은 1_000_000원입니다.")
     * 충전 시 보유잔고가 5_000_000 초과시 실패 => IllegalStateException("보유 잔고는 5_000,000원을 초과할 수 없습니다.")
     */
    public Point charge(ChargePointCommand command);
    public Point getPointByUserId(GetPointCommand command);
}
