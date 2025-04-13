package kr.hhplus.be.server.application.point;

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

    public Optional<PointResult> getPointByUserId(Long userId) {
        return pointService.getPointByUserIdWithEmpty(userId)
                .map(PointResult::of);
    }

    public PointResult charge(ChargePointCommand command) {
        var user = userService.getUserById(command.userId());
        var point = pointService.getPointByUserId(user.getId());
        return PointResult.of(pointService.charge(point, command.amount()));
    }

    public PointResult use(UsePointCommand command) {
        var user = userService.getUserById(command.userId());
        var point = pointService.getPointByUserId(user.getId());
        return PointResult.of(pointService.use(point, command.amount()));
    }
}
