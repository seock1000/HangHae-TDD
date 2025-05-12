package kr.hhplus.be.server.domain.bestseller;


import jakarta.persistence.*;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSeller extends BaseTimeEntity {
    @EmbeddedId
    private BestSellerId id;
    private Long productId;
    private int salesAmount;
    private LocalDate date;

    private BestSeller(SalesStat salesStat, LocalDate date) {
        this.id = BestSellerId.createWithProduct(salesStat);
        this.productId = salesStat.getProductId();
        this.salesAmount = salesStat.getAmount();
        this.date = date;
    }

    public static BestSeller createWithSalesStatAndDate(SalesStat stat, LocalDate date) {
        return new BestSeller(stat, date);
    }
}
