package io.hhplus.tdd.point.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChargePointDtoTest {

    /**
     * ChangePointDto 생성 테스트 - 성공
     * 생성된 것 자체가 검증이라고 생각해서 값의 검증은 하지 않았습니다.
     */
    @Test
    @DisplayName("ChangePointDto 생성 시, 유저 ID와 충전액이 1이상이면 충전에 성공한다.")
    void createChargePoint() {
        //given
        long userId = 1L;
        long amount = 1L;

        //when & then
        ChargePointDto chargePointDto = new ChargePointDto(userId, amount);
    }

    /**
     * ChangePointDto 생성 테스트 - 잘못된 유저 ID
     * 유저 ID가 형식상 옳지 않으면 예외를 반환합니다.
     */
    @Test
    @DisplayName("ChargePointDto 생성 시, 유저 ID가 1 미만이면 IllegalArgumentException을 throw 한다.")
    void invalidUserId() {
        //given
        long userId = 0L;
        long amount = 1L;

        //when & then
        assertThatThrownBy(() -> new ChargePointDto(userId, amount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * ChangePointDto 생성 테스트 - 잘못된 충전액
     * 충전액이 형식상 옳지 않으면 예외를 반환합니다.
     */
    @Test
    @DisplayName("ChargePointDto 생성 시, 충전액이 1 미만이면 IllegalArgumentException을 throw 한다.")
    void invalidAmount() {
        //given
        long userId = 1L;
        long amount = 0L;

        //when & then
        assertThatThrownBy(() -> new ChargePointDto(userId, amount))
                .isInstanceOf(IllegalArgumentException.class);
    }
}