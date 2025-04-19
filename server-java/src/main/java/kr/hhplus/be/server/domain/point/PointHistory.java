package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;
    private int amount;
    private int balance;
    private TransactionType type;

    private PointHistory(Point point, int amount, int balance, TransactionType type) {
        this.point = point;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    public static PointHistory createChargeHistory(Point point, int amount) {
        return new PointHistory(point, amount, point.getBalance(), TransactionType.CHARGE);
    }

    public static PointHistory createUseHistory(Point point, int amount) {
        return new PointHistory(point, amount, point.getBalance(), TransactionType.USE);
    }
}
