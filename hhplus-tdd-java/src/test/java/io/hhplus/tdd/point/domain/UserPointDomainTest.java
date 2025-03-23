package io.hhplus.tdd.point.domain;

import io.hhplus.tdd.point.domain.error.ExceedMaxPointError;
import io.hhplus.tdd.point.domain.model.UserPointDomainFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserPointDomainTest {

    @Test
    @DisplayName("포인트 충전")
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

    @Test
    @DisplayName("포인트 충전_최대 포인트를 초과하면 ExceedMaxPointError를 발생시킨다.")
    void chargeExceedMaxPoint() {
        //given
        UserPointDomain userPointDomain = UserPointDomainFixture.initByZero();
        long chargePoint = PointConstant.MAX_POINT + 1;

        //when & then
        assertThatThrownBy(() -> userPointDomain.charge(chargePoint))
                .isInstanceOf(ExceedMaxPointError.class);
    }
}