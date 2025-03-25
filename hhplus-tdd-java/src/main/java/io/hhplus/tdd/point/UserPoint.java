package io.hhplus.tdd.point;

import io.hhplus.tdd.point.domain.UserPointDomain;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPointDomain toDomain() {
        return new UserPointDomain(this.id, this.point);
    }
}
