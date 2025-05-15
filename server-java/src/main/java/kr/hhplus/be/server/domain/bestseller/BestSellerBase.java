package kr.hhplus.be.server.domain.bestseller;

import jakarta.persistence.*;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderSalesAmount;
import kr.hhplus.be.server.domain.order.Orders;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BestSellerBase extends BaseTimeEntity {
    @EmbeddedId
    private BestSellerBaseId id;
    private Long productId;
    private int salesAmount;
    private LocalDate date;

    @Deprecated
    private BestSellerBase(OrderSalesAmount salesAmount, LocalDate date) {
        this.id = BestSellerBaseId.createWithProduct(salesAmount);
        this.productId = salesAmount.getProductId();
        this.salesAmount = salesAmount.getAmount();
        this.date = date;
    }

    private BestSellerBase(SalesStat salesStat, LocalDate date) {
        this.id = BestSellerBaseId.createWithSalesStat(salesStat);
        this.productId = salesStat.getProductId();
        this.salesAmount = salesStat.getAmount();
        this.date = date;
    }

    @Deprecated
    public static BestSellerBase createWithSalesAmountAndDate(OrderSalesAmount salesAmount, LocalDate date) {
        return new BestSellerBase(salesAmount, date);
    }

    public static BestSellerBase createWithSalesStatAndDate(SalesStat salesStat, LocalDate date) {
        return new BestSellerBase(salesStat, date);
    }

    public void addSalesAmount(int amount) {
        this.salesAmount += amount;
    }
}
