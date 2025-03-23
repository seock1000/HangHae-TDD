package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.domain.error.ExceedMaxPointError;
import io.hhplus.tdd.point.domain.error.UnderMinPointError;
import lombok.Getter;

@Getter
public class UserPointDomain {
    private long id;
    private long point;
    private long updateMillis;

    // 최소 포인트
    private static final long MIN_POINT = 0L;
    // 최대 포인트
    private static final long MAX_POINT = 5000L;

    /**
     * 포인트 충전
     * @param 충전액수
     */
    public void charge(long amount) {
        if((this.point + amount) > MAX_POINT) {
            throw new ExceedMaxPointError();
        }
        this.point += amount;
        this.updateMillis = System.currentTimeMillis();
    }

    /**
     * 포인트 사용
     * @param 사용액수
     */
    public void use(long amount) {
        if((this.point - amount) < MIN_POINT) {
            throw new UnderMinPointError();
        }
        this.point -= amount;
        this.updateMillis = System.currentTimeMillis();
    }
}
