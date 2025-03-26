package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.ChargePointDto;
import io.hhplus.tdd.point.dto.UsePointDto;

import java.util.List;

public interface PointService {

    public UserPoint readUserPoint(long userId);

    public List<PointHistory> readPointHistoryByUserId(long userId);

    public UserPoint chargeUserPoint(ChargePointDto chargePointDto);

    public UserPoint useUserPoint(UsePointDto usePointDto);
}
