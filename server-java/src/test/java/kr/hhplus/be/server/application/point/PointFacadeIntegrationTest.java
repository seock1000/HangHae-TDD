package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.point.PointHistoryJpaRepository;
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class PointFacadeIntegrationTest {

    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private PointJpaRepository pointJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private PointHistoryJpaRepository pointHistoryJpaRepository;

    @Test
    @DisplayName("포인트 조회 시, 받은 유저 ID에 대한 포인트가 없으면 Optional.empty()를 반환한다.")
    void getPointByUserIdWithEmpty() {
        // given
        long userId = 1L;

        // when
        var result = pointFacade.getPointByUserId(userId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("포인트 조회 시, 받은 유저 ID에 대한 포인트가 있으면 Optional.of(PointResult)를 반환한다.")
    void getPointByUserIdWithPoint() {
        // given
        long userId = 1L;
        var point = Instancio.of(Point.class)
                .set(field("id"), null)
                .set(field("userId"), userId)
                .set(field("balance"), 1000)
                .set(field("histories"), new ArrayList<>())
                .create();
        pointJpaRepository.save(point);

        // when
        var result = pointFacade.getPointByUserId(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(1000, result.get().balance());
    }

    @Test
    @DisplayName("포인트 충전 시, 포인트가 충전되고 충전내역이 생성된다.")
    void charge() {
        // given
        var user = userJpaRepository.saveAndFlush(
                Instancio.of(User.class)
                .set(field("id"), null)
                .create());

        pointJpaRepository.saveAndFlush(
                Instancio.of(Point.class)
                        .set(field("id"), null)
                        .set(field("userId"), user.getId())
                        .set(field("balance"), 1000)
                        .set(field("histories"), new ArrayList<>())
                        .create());

        var command = new ChargePointCommand(user.getId(), 500);

        // when
        var result = pointFacade.charge(command);

        // then
        assertEquals(1500, result.balance());
        assertEquals(1, pointHistoryJpaRepository.count());
    }

}