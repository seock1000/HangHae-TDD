package kr.hhplus.be.server.domain.product;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Product {

    private Long id;
    private String title;
    private String description;
    private int price;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
