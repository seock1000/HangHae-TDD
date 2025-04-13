package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Point {
    private Long id;
    private long user;
    private int balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Point(Long id, long user, int balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    /**
     * TC
     * 포인트를 충전한다.
     * 충전 금액이 1,000,000원을 초과할 경우 ApiException(EXCEED_CHARGE_LIMIT)을 발생시킨다.
     * 충전 후 잔액이 5,000,000원을 초과할 경우 ApiException(EXCEED_BALANCE_LIMIT)을 발생시킨다.
     */
    public void charge(int amount) {
        if(amount > 1_000_000) {
            throw ApiException.of(ApiError.EXCEED_CHARGE_LIMIT);
        }
        if(this.balance + amount > 5_000_000) {
            throw ApiException.of(ApiError.EXCEED_BALANCE_LIMIT);
        }
        this.balance += amount;
    }

    /**
     * TC
     * 포인트를 사용한다.
     * 사용 금액이 잔액을 초과할 경우 ApiException(UNDER_BALANCE_LIMIT)을 발생시킨다.
     */
    public void use(int amount) {
        if(this.balance - amount < 0) {
            throw ApiException.of(ApiError.UNDER_BALANCE_LIMIT);
        }
        this.balance -= amount;
    }

}
