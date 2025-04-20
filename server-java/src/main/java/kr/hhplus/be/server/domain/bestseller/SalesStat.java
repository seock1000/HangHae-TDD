package kr.hhplus.be.server.domain.bestseller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesStat {
    private Long productId;
    private int amount;

    public SalesStat(Long productId, Long amount) {
        this.productId = productId;
        this.amount = amount.intValue();
    }
}
