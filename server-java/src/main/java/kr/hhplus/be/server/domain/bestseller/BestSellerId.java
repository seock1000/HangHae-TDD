package kr.hhplus.be.server.domain.bestseller;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class BestSellerId implements Serializable {

    private String id;

    private BestSellerId(String id) {
        this.id = id;
    }

    public static BestSellerId createWithProduct(SalesStat salesStat) {
        if (salesStat == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return new BestSellerId(System.currentTimeMillis() + "_" + salesStat.getProductId().toString());
    }
}
