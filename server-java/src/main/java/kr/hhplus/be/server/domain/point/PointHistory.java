package kr.hhplus.be.server.domain.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class PointHistory {
    private Long id;
    private Point point;
    private int amount;
    private int balance;
    private TransactionType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PointHistory(Point point, int amount, TransactionType type) {
        this.point = point;
        this.amount = amount;
        this.balance = point.getBalance();
        this.type = type;
    }

    public static PointHistory createChargeHistory(Point point, int amount) {
        return new PointHistory(point, amount, TransactionType.CHARGE);
    }

    public static PointHistory createUseHistory(Point point, int amount) {
        return new PointHistory(point, amount, TransactionType.USE);
    }
}
