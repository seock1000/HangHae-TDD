package kr.hhplus.be.server.point.controller.in;

public record ChargePointRequest(
        Long userId,
        Integer amount
){
}
