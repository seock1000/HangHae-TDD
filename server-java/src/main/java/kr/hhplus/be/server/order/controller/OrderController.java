package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.BaseResponse;
import kr.hhplus.be.server.order.controller.in.CreateOrderProductRequest;
import kr.hhplus.be.server.order.controller.in.CreateOrderRequest;
import kr.hhplus.be.server.order.controller.out.CreateOrderResponse;
import kr.hhplus.be.server.order.controller.spec.OrderSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController implements OrderSpec {

    /**
     * 발생 가능 예외
     * 400 : 유효하지 않은 사용자입니다.
     * 400 : 유효하지 않은 상품입니다.
     * 400 : 유효하지 않은 상품 수량입니다.
     * 409 : 유효하지 않은 쿠폰인 경우 --> 컨트롤레에서 테스트하는 것이 아닌 것 같습니다.
     * 409 : 재고가 부족한 경우 --> 컨트롤레에서 테스트하는 것이 아닌 것 같습니다.
     */
    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<CreateOrderResponse>> getBestSellers(
            @RequestBody CreateOrderRequest request
    ) {
        if(request.userId() == null || request.userId() <= 0) {
            return ResponseEntity.badRequest().body(
                    BaseResponse.fail(BAD_REQUEST, "유효하지 않은 사용자입니다."));
        }

        for(CreateOrderProductRequest orderProduct : request.orderProducts()) {
            if(orderProduct.productId() == null || orderProduct.productId() <= 0) {
                return ResponseEntity.badRequest().body(
                        BaseResponse.fail(BAD_REQUEST, "유효하지 않은 상품입니다."));
            }
            if(orderProduct.quantity() == null || orderProduct.quantity() <= 0) {
                return ResponseEntity.badRequest().body(
                        BaseResponse.fail(BAD_REQUEST, "유효하지 않은 상품 수량입니다."));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                BaseResponse.created(new CreateOrderResponse(1L))
        );
    }
}
