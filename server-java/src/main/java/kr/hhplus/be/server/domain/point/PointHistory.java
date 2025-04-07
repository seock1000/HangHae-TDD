package kr.hhplus.be.server.domain.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PointHistory {
    private Long id;
    private Long point;
    private int amount;
    private int balance;
    private TransactionType type;

    private PointHistory(Long point, int amount, int balance, TransactionType type) {
        this.point = point;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    public static PointHistory createChargeHistory(Long point, int amount, int curBalance) {
        return new PointHistory(point, amount, curBalance, TransactionType.CHARGE);
    }

    public static PointHistory createUseHistory(Long point, int amount, int curBalance) {
        return new PointHistory(point, amount, curBalance, TransactionType.USE);
    }
}
