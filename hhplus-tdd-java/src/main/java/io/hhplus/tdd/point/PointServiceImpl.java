package io.hhplus.tdd.point;

import io.hhplus.tdd.DomainException;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.dto.ChargePointDto;
import io.hhplus.tdd.point.dto.UsePointDto;
import io.hhplus.tdd.point.util.UserPointLockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    // lock 객체 생성
    private final UserPointLockManager lockManager;

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
        UserPointDomain userPointDomain;
        UserPoint updatedUserPoint;
        try {
            lockManager.lock(dto.userId());
            userPointDomain = userPointTable.selectById(dto.userId()).toDomain();
            userPointDomain.charge(dto.amount());

            updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
        } finally {
            lockManager.release(dto.userId());
        }
        pointHistoryTable.insert(
                updatedUserPoint.id(),
                updatedUserPoint.point(),
                TransactionType.CHARGE,
                updatedUserPoint.updateMillis()
        );

        return updatedUserPoint;
    }

    @Override
    public UserPoint useUserPoint(UsePointDto dto) {
        UserPointDomain userPointDomain;
        UserPoint updatedUserPoint;
        try {
            lockManager.lock(dto.userId());
            userPointDomain = userPointTable.selectById(dto.userId()).toDomain();
            userPointDomain.use(dto.amount());

            updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
        } finally {
            lockManager.release(dto.userId());
        }
        pointHistoryTable.insert(
                updatedUserPoint.id(),
                updatedUserPoint.point(),
                TransactionType.USE,
                updatedUserPoint.updateMillis()
        );

        return updatedUserPoint;
    }
}
