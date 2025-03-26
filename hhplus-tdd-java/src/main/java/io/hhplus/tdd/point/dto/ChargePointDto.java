package io.hhplus.tdd.point.dto;

public record ChargePointDto(
        long userId,
        long amount
){
    public ChargePointDto {
        if(userId < 1L) {
            throw new IllegalArgumentException("유저 ID는 1 미만일 수 없습니다.");
        }
        if(amount < 1L) {
            throw new IllegalArgumentException("충전액은 1 미만일 수 없습니다.");
        }
    }
}
