package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiError;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long user;
    private int balance;
    @Transient
    private List<PointHistory> histories = new ArrayList<>();

    public Point(Long id, long user, int balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    /**
     * TC
     * 포인트를 충전하고 충전 내역을 생성한다.
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
        this.histories.add(PointHistory.createChargeHistory(this, amount));
    }

    /**
     * 테스트 필요없을 듯
     */
    public UsedPoint toUsedPoint() {
        return UsedPoint.of(this);
    }

    /**
     * TC
     * 포인트를 사용하고 사용 내역을 생성한다.
     * 사용 금액이 잔액을 초과할 경우 ApiException(UNDER_BALANCE_LIMIT)을 발생시킨다.
     */
    protected void use(int amount) {
        if(this.balance - amount < 0) {
            throw ApiException.of(ApiError.UNDER_BALANCE_LIMIT);
        }
        this.balance -= amount;
        this.histories.add(PointHistory.createUseHistory(this, amount));
    }
}
