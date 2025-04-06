package kr.hhplus.be.server.domain.point;

import lombok.*;

@Getter
@EqualsAndHashCode
public class Point {
    private long id;
    private long userId;
    private int balance;

    public Point(long id, long userId, int balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }
}
