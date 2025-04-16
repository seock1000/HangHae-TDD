package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.config.jpa.BaseTimeEntity;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.SoldProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders order;
    private Long productId;
    private int amount;
    private int price;
    private int quantity;

    private OrderItem(Orders order, Long productId, int amount, int price, int quantity) {
        this.order = order;
        this.productId = productId;
        this.amount = amount;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItem create(Orders order, SoldProduct product, int quantity) {
        return new OrderItem(order, product.getId(), product.getPrice() * quantity, product.getPrice(), quantity);
    }
}
