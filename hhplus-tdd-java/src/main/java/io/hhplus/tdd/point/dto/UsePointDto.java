package io.hhplus.tdd.point.dto;

public record UsePointDto(
        long userId,
        long amount
){
    public UsePointDto {
        if(userId < 1L) {
            throw new IllegalArgumentException("유저 ID는 1 미만일 수 없습니다.");
        }
        if(amount < 1L) {
            throw new IllegalArgumentException("사용액은 1 미만일 수 없습니다.");
        }
    }
}
