package kr.hhplus.be.server.presentation.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.PlaceOrderCommand;

import java.util.List;

public record CreateOrderRequest(
        @Schema(description = "사용자 ID")
        Long userId,
        @Schema(description = "사용자 쿠폰 ID")
        Long userCouponId,
        @Schema(description = "주문 상품 리스트")
        List<CreateOrderProductRequest> orderProducts
) {
        public PlaceOrderCommand toCommand() {
                return new PlaceOrderCommand(
                        userId,
                        userCouponId,
                        orderProducts.stream()
                                .map(CreateOrderProductRequest::toCommand)
                                .toList()
                );
        }
}
