package kr.hhplus.be.server.domain.bestseller;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSeller extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private int salesAmount;
    private LocalDate date;

    private BestSeller(Long productId, int salesAmount, LocalDate date) {
        this.productId = productId;
        this.salesAmount = salesAmount;
        this.date = date;
    }

    public static BestSeller createWithSalesStatAndDate(SalesStat stat, LocalDate date) {
        return new BestSeller(stat.getProductId(), stat.getAmount(), date);
    }
}
