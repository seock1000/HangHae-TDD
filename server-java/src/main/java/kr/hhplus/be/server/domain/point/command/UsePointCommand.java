package kr.hhplus.be.server.domain.point.command;

import kr.hhplus.be.server.domain.point.error.InvalidAmountError;
import kr.hhplus.be.server.domain.point.error.InvalidUserIdError;

/**
 * TC
 * userId가 0보다 작거나 같으면 실패한다 => InvalidUserIdError
 * amount가 0보다 작거나 같으면 실패한다 => InvalidAmountError
 */
public record UsePointCommand(
        long userId,
        int amount
) {
    public UsePointCommand {
        if (userId <= 0) {
            throw InvalidUserIdError.of("잘못된 사용자 ID 형식입니다.");
        }
        if (amount <= 0) {
            throw InvalidAmountError.of("잘못된 사용 금액 형식입니다.");
        }
    }
}
