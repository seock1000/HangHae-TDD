package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.domain.error.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static io.hhplus.tdd.point.domain.PointConstant.*;

@Getter
@AllArgsConstructor
public class UserPointDomain {
    private long id;
    private long point;
    private long updateMillis;

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
