package kr.hhplus.be.server.presentation.payment;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.PaymentLockFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentSpec {

    private final PaymentLockFacade paymentFacade;

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
        paymentFacade.pay(request.toCommand());

        return ResponseEntity.ok(BaseResponse.success());
    }
}
