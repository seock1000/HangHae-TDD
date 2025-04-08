package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.error.InvalidAmountError;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;

/**
 * TC
 * point가 null이면 실패한다 => PointNotExistError
 * amount가 0보다 작거나 같으면 실패한다 => InvalidAmountError
 */
public record UsePointCommand(
        Point point,
        int amount
) {
    public UsePointCommand {
        if (point == null) {
            throw PointNotExistError.of("포인트 정보가 존재하지 않습니다.");
        }
        if (amount <= 0) {
            throw InvalidAmountError.of("잘못된 사용 금액 형식입니다.");
        }
    }
}
