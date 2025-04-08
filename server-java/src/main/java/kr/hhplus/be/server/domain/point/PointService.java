package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.command.ChargePointCommand;
import kr.hhplus.be.server.domain.point.command.UsePointCommand;
import kr.hhplus.be.server.domain.point.error.PointNotExistError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 검증은 도메인에서 완료하였으므로, TC 존재하지 않음 -> 해피케이스만 작성
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * TC
     * userId로 포인트 정보를 조회하여 포인트를 충전하고, 충전 이력을 저장한 뒤 포인트를 반환한다.
     * 포인트 정보가 존재하지 않으면 PointNotExistError를 발생시킨다.
     */
    public Point charge(ChargePointCommand cmd) {
        Point point = pointRepository.findByUserId(cmd.userId())
                .orElseThrow(() -> PointNotExistError.of("포인트 정보가 존재하지 않습니다."));

        point.charge(cmd.amount());
        pointHistoryRepository.save(
                PointHistory.createChargeHistory(point, cmd.amount())
        );

        return pointRepository.save(point);
    }

    /**
     * TC
     * userId로 포인트 정보를 조회하여 포인트를 사용하고, 사용 이력을 저장한 뒤 포인트를 반환한다.
     * 포인트 정보가 존재하지 않으면 PointNotExistError를 발생시킨다.
     */
    public Point use(UsePointCommand cmd) {
        Point point = pointRepository.findByUserId(cmd.userId())
                .orElseThrow(() -> PointNotExistError.of("포인트 정보가 존재하지 않습니다."));

        point.use(cmd.amount());
        pointHistoryRepository.save(
                PointHistory.createUseHistory(point, cmd.amount())
        );

        return pointRepository.save(point);
    }
}
