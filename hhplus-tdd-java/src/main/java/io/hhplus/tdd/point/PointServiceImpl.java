package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.dto.ChargePointDto;
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
    public UserPoint chargeUserPoint(ChargePointDto dto) {
        UserPointDomain userPointDomain = userPointTable.selectById(dto.userId()).toDomain();
        userPointDomain.charge(dto.amount());

        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
        pointHistoryTable.insert(
                updatedUserPoint.id(),
                updatedUserPoint.point(),
                TransactionType.CHARGE,
                updatedUserPoint.updateMillis()
        );

        return updatedUserPoint;
    }

    @Override
    public UserPoint useUserPoint(long userId, long amount) {
        UserPointDomain userPointDomain = userPointTable.selectById(userId).toDomain();
        userPointDomain.use(amount);

        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
        pointHistoryTable.insert(
                updatedUserPoint.id(),
                updatedUserPoint.point(),
                TransactionType.USE,
                updatedUserPoint.updateMillis()
        );

        return updatedUserPoint;
    }
}
