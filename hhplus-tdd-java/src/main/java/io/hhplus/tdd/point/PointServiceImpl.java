package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.domain.UserPointDomain;
import io.hhplus.tdd.point.dto.ChargePointDto;
import io.hhplus.tdd.point.dto.UsePointDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    // lock 객체 생성
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public UserPoint readUserPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    @Override
    public List<PointHistory> readPointHistoryByUserId(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }

    // 작업 중간 에러가 발생하면 이후의 작업이 수행되면 안되므로 try 문으로 묶음
    @Override
    public UserPoint chargeUserPoint(ChargePointDto dto) {
        lock.lock();
        try {
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
        } finally {
            lock.unlock();
        }
    }

    // 작업 중간 에러가 발생하면 이후의 작업이 수행되면 안되므로 try 문으로 묶음
    @Override
    public UserPoint useUserPoint(UsePointDto dto) {
        lock.lock();
        try {
            UserPointDomain userPointDomain = userPointTable.selectById(dto.userId()).toDomain();
            userPointDomain.use(dto.amount());

            UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userPointDomain.getId(), userPointDomain.getPoint());
            pointHistoryTable.insert(
                    updatedUserPoint.id(),
                    updatedUserPoint.point(),
                    TransactionType.USE,
                    updatedUserPoint.updateMillis()
            );

            return updatedUserPoint;
        } finally {
            lock.unlock();
        }
    }
}
