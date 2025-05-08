package kr.hhplus.be.server.presentation.order;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderLockFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderSpec {

    private final OrderLockFacade orderFacade;

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 400 : 유효하지 않은 상품입니다.
     * 400 : 유효하지 않은 상품 수량입니다.
     * 409 : 유효하지 않은 쿠폰인 경우 --> 컨트롤레에서 테스트하는 것이 아닌 것 같습니다.
     * 409 : 재고가 부족한 경우 --> 컨트롤레에서 테스트하는 것이 아닌 것 같습니다.
     */
    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<CreateOrderResponse>> createOrder(
            @RequestBody CreateOrderRequest request
    ) {
        OrderResult result = orderFacade.placeOrder(request.toCommand());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(CreateOrderResponse.of(result))
        );
    }
}
