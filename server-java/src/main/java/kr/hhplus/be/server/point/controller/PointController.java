package kr.hhplus.be.server.point.controller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.point.controller.in.ChargePointRequest;
import kr.hhplus.be.server.point.controller.out.ChargePointResponse;
import kr.hhplus.be.server.point.controller.out.GetPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 409 : 1회 최대 충전 금액 초과
     * 409 : 최대 잔고 초과
     */
    @PostMapping("/charge")
    public ResponseEntity<BaseResponse<ChargePointResponse>> charge(
            @RequestBody ChargePointRequest request
            ) {
        if(request.userId() == null || request.userId() <= 0) {
            return ResponseEntity.status(BAD_REQUEST).body(BaseResponse.fail(BAD_REQUEST, "유효하지 않은 사용자입니다."));
        }
        if(request.amount() > 5_000_000) {
            return ResponseEntity.status(CONFLICT).body(BaseResponse.fail(CONFLICT, "최대 잔고를 초과 했습니다."));
        }
        if(request.amount() > 1_000_000) {
            return ResponseEntity.status(CONFLICT).body(BaseResponse.fail(CONFLICT, "1회 최대 충전금액을 초과했습니다."));
        }
        return ResponseEntity.ok(
                BaseResponse.success(new ChargePointResponse(request.userId(), request.amount()))
        );
    }


    /**
     * 발생 가능 예외 X
     */
    @GetMapping
    public ResponseEntity<BaseResponse<List<GetPointResponse>>> getPoint(
            @RequestParam("userId") Long userId
    ) {
        return ResponseEntity.ok(
                BaseResponse.success(List.of(new GetPointResponse(userId, 0)))
        );
    }

}
