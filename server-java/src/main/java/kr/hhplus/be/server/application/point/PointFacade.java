package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.config.redis.DistributedLock;
import kr.hhplus.be.server.config.redis.LockMethod;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointFacade {
    private final UserService userService;
    private final PointService pointService;

    @Transactional(readOnly = true)
    public Optional<PointResult> getPointByUserId(Long userId) {
        return pointService.getPointByUserIdWithEmpty(userId)
                .map(PointResult::of);
    }

    @DistributedLock(key = "'point:user:' + #command.userId()", method = LockMethod.PUBSUB)
    public PointResult charge(ChargePointCommand command) {
        var user = userService.getUserById(command.userId());
        var point = pointService.getPointByUserId(user.getId());

        point.charge(command.amount());
        pointService.save(point);
        return PointResult.of(point);
    }
}
