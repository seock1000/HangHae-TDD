package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.domain.error.ExceedMaxPointError;
import io.hhplus.tdd.point.domain.error.UnderMinPointError;
import io.hhplus.tdd.point.domain.model.UserPointDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserPointDomainTest {

    /**
     * 포인트 충전 성공 TC
     * 포인트는 정책상 최대 잔고까지 충전 가능해야 한다.
     */
    @Test
    @DisplayName("포인트 충전_포인트 충전에 성공하면 충전액과 합산된 값으로 보유 포인트가 업데이트 된다.")
    void charge() {
        //given
        UserPointDomain userPointDomain = UserPointDomainFixture.initByZero();
        long chargePoint = PointConstant.MAX_POINT;
        long expectedPoint = chargePoint + userPointDomain.getPoint();

        //when
        userPointDomain.charge(chargePoint);

        //then
        assertThat(userPointDomain.getPoint()).isEqualTo(expectedPoint);
    }

    /**
     * 포인트 충전 실패 TC - 최대 잔고 초과
     * 정책상 최대 잔고를 초과하는 경우, 충전에 실패하며 ExceedMaxPointError를 발생시킨다.
     */
    @Test
    @DisplayName("포인트 충전_보유포인트와 충전액과의 합산 값이 최대 포인트를 초과하면 ExceedMaxPointError를 발생시킨다.")
    void chargeExceedMaxPoint() {
        //given
        UserPointDomain userPointDomain = UserPointDomainFixture.initByZero();
        long chargePoint = PointConstant.MAX_POINT + 1;

        //when & then
        assertThatThrownBy(() -> userPointDomain.charge(chargePoint))
                .isInstanceOf(ExceedMaxPointError.class);
    }

    /**
     * 포인트 사용 성공 TC
     * 포인트는 정책상 최소 잔고까지 사용 가능해야 한다.
     */
    @Test
    @DisplayName("포인트 사용_포인트 사용에 성공하면 사용액을 차감한 값으로 보유 포인트가 업데이트 된다.")
    void use() {
        //given
        UserPointDomain userPointDomain = UserPointDomainFixture.initByMax();
        long usePoint = PointConstant.MAX_POINT - PointConstant.MIN_POINT;
        long expectedPoint = userPointDomain.getPoint() - usePoint;

        //when
        userPointDomain.use(usePoint);

        //then
        assertThat(userPointDomain.getPoint()).isEqualTo(expectedPoint);
    }

    /**
     * 포인트 사용 실패 TC - 최소 잔고 미만
     * 포인트 사용 시 정책상 최소 잔고 미만이 되는 경우, 사용에 실패하며 UnderMinPointError를 발생시킨다.
     */
    @Test
    @DisplayName("포인트 사용_보유 포인트에서 사용액을 차감한 값이 최소 포인트 미만이면 UnderMinPointError를 발생시킨다.")
    void useUnderMinPoint() {
        //given
        UserPointDomain userPointDomain = UserPointDomainFixture.initByMin();
        long usePoint = 1;

        //when & then
        assertThatThrownBy(() -> userPointDomain.use(usePoint))
                .isInstanceOf(UnderMinPointError.class);
    }
}