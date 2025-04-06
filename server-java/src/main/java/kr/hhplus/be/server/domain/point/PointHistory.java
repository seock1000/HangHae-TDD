package kr.hhplus.be.server.domain.point;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PointHistory {
    private long id;
    private long pointId;
    private int amount;
    private int balance;
    private TransactionType type;
}
