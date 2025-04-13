package kr.hhplus.be.server.domain.point;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class Point {
    private Long id;
    private long user;
    private int balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Point(Long id, long user, int balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

}
