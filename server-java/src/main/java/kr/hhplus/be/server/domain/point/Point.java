package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
public class Point {
    private long id;
    private long user;
    private int balance;
    private List<PointHistory> pointHistoryList;

    public Point(long id, long user, int balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }
}
