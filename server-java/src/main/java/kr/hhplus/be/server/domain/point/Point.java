package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.error.ExceedMaxBalanceError;
import kr.hhplus.be.server.domain.point.error.InsufficientBalanceError;
import kr.hhplus.be.server.domain.point.error.InvalidChargeAmountError;
import lombok.*;

@Getter
@EqualsAndHashCode
public class Point {
    private Long id;
    private long user;
    private int balance;

    public Point(Long id, long user, int balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    /**
     * TC
     * 1회 충전 금액이 1_000_000을 넘으면 실패 => InvalidChargeAmountException
     * 충전 시 잔고가 5_000_000을 넘으면 실패 => ExceedMaxBalanceException
     */
    public void charge(int amount) {
        if (amount > 1_000_000) {
            throw InvalidChargeAmountError.of("1회 최대 충전 금액은 1,000,000원입니다.");
        }
        if(balance + amount > 5_000_000) {
            throw ExceedMaxBalanceError.of("보유 잔고는 5,000,000원을 초과할 수 없습니다.");
        }
        this.balance += amount;
    }

    /**
     * TC
     * 포인트 사용 시 잔고가 부족하면 실패 => IllegalArgumentException
     */
    public void use(int amount) {
        if (this.balance < amount) {
            throw InsufficientBalanceError.of("잔고가 부족합니다.");
        }
        this.balance -= amount;
    }
}
