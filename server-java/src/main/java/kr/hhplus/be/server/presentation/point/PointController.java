package kr.hhplus.be.server.presentation.point;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController implements PointSpec {

    private final PointFacade pointFacade;

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 409 : 1회 최대 충전 금액 초과
     * 409 : 최대 잔고 초과
     */
    @Override
    @PostMapping("/charge")
    public ResponseEntity<BaseResponse<ChargePointResponse>> charge(
            @RequestBody ChargePointRequest request
            ) {
        PointResult result = pointFacade.charge(request.toCommand());

        return ResponseEntity.ok(
                BaseResponse.success(ChargePointResponse.of(result))
        );
    }


    /**
     * 발생 가능 예외 X
     */
    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<GetPointResponse>> getPoint(
            @RequestParam("userId") Long userId
    ) {
        Optional<PointResult> result = pointFacade.getPointByUserId(userId);

        return result.map(pointResult -> ResponseEntity.ok(
                BaseResponse.success(GetPointResponse.of(pointResult))
        )).orElseGet(() -> ResponseEntity.ok(null));
    }

}
