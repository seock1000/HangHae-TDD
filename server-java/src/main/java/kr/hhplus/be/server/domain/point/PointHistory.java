package kr.hhplus.be.server.domain.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode
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

    public static PointHistory createChargeHistory(Point point, int amount) {
        return new PointHistory(point.getId(), amount, point.getBalance(), TransactionType.CHARGE);
    }

    public static PointHistory createUseHistory(Point point, int amount) {
        return new PointHistory(point.getId(), amount, point.getBalance(), TransactionType.USE);
    }
}
