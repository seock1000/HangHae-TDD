package io.hhplus.tdd.point;

import java.util.List;

public interface PointService {

    public UserPoint readUserPoint(long userId);

    public List<PointHistory> readPointHistoryByUserId(long userId);

    public UserPoint chargeUserPoint(long userId, long amount);
}
