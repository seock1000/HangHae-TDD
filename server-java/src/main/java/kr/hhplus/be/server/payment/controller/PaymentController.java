package kr.hhplus.be.server.payment.controller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.payment.controller.in.PayRequest;
import kr.hhplus.be.server.payment.controller.spec.PaymentSpec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController implements PaymentSpec {

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 주문입니다.
     * 409 : 잔여 포인트가 부족합니다. --> 컨트롤러 level에서 테스트하는게 아닌 것 같습니다.
     * 409 : 취소된 주문입니다. --> 컨트롤러 level에서 테스트하는게 아닌 것 같습니다.
     */
    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> pay(
            @RequestBody PayRequest request
            ) {
        if(request.orderId() == null || request.orderId() < 1L) {
            return ResponseEntity.badRequest().body(BaseResponse.fail(BAD_REQUEST, "유효하지 않은 주문입니다."));
        }

        return ResponseEntity.ok(BaseResponse.success());
    }
}
