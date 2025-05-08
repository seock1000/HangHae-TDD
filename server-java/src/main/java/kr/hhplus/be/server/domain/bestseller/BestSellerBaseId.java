package kr.hhplus.be.server.domain.bestseller;

import jakarta.persistence.Embeddable;
import kr.hhplus.be.server.domain.order.OrderSalesAmount;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class BestSellerBaseId implements Serializable {

    private String id;

    private BestSellerBaseId(String id) {
        this.id = id;
    }

    public static BestSellerBaseId createWithProduct(OrderSalesAmount salesAmount) {
        if (salesAmount == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return new BestSellerBaseId(System.currentTimeMillis() + "_" + salesAmount.getProductId().toString());
    }
}
