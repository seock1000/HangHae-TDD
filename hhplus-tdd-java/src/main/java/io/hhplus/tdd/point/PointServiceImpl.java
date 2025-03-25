package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPointDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    @Override
    public UserPoint readUserPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    @Override
    public List<PointHistory> readPointHistoryByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

    @Override
    public UserPoint chargeUserPoint(long userId, long amount) {
        UserPointDomain userPointDomain = userPointTable.selectById(userId).toDomain();
        userPointDomain.charge(amount);

        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
        pointHistoryTable.insert(
                updatedUserPoint.id(),
                updatedUserPoint.point(),
                TransactionType.CHARGE,
                updatedUserPoint.updateMillis()
        );

        return updatedUserPoint;
    }
}
