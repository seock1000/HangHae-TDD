package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.point.error.PointInfoNotFoundError;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointFacade {

        private final PointService pointService;
        private final PointRepository pointRepository;

        /**
         * TC
         * 존재하는 포인트 정보를 조회하면 해당 포인트 정보를 반환한다.
         * 존재하지 않는 포인트 정보를 조회하면 Optional.empty() 를 반환한다.
         */
        public Optional<PointResult> getByUserId(GetPointCriteria criteria) {
                return pointRepository.findByUserId(criteria.userId())
                        .map(point -> new PointResult(point.getId(), point.getBalance()));
        }

        /**
         * TC
         * 포인트 내역이 존재하지 않는 경우 : PointInfoNotFoundError 발생
         * 이외에는 도메인, 도메인 서비스에서 검증 완료
         */
        public PointResult charge(ChargePointCriteria criteria) {
                Point point = pointRepository.findByUserId(criteria.userId())
                        .orElseThrow(() -> PointInfoNotFoundError.of("존재하지 않는 포인트 정보입니다."));

                point = pointRepository.save(
                        pointService.charge(point, criteria.amount())
                );

                pointRepository.saveHistory(
                        pointService.createChargeHistory(point, criteria.amount())
                );

                return new PointResult(point.getId(), point.getBalance());
        }
}
