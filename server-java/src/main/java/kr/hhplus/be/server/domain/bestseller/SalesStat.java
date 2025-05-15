package kr.hhplus.be.server.domain.bestseller;

import kr.hhplus.be.server.domain.order.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesStat {
    private Long productId;
    private Integer amount;

    public SalesStat(Long productId, Long amount) {
        this.productId = productId;
        this.amount = amount.intValue();
    }

    public static SalesStat of(OrderItem orderItem) {
        return new SalesStat(orderItem.getProductId(), (long) orderItem.getQuantity());
    }
}
