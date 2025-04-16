package kr.hhplus.be.server.domain.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSalesAmount {
    private Long productId;
    private int amount;

    public OrderSalesAmount(Long productId, Long amount) {
        this.productId = productId;
        this.amount = amount.intValue();
    }
}
