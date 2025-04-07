package kr.hhplus.be.server.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public Optional<Point> getPointByUserId(GetPointCommand command) {
        return pointRepository.findByUserId(command.userId());
    }

    @Override
    public Point charge(ChargePointCommand command) {
        return null;
    }
}
