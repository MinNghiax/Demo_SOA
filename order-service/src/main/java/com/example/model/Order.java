package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    // GETTERS & SETTERS
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "user_id")  // 🔥 BẮT BUỘC PHẢI CÓ
    private Long userId;

    @Setter
    private String product;

    @Setter
    private Double amount;

    @Column(name = "created_at", insertable = false, updatable = false) // optional
    private String createdAt;

    public Order() {}

    public Order(Long id, Long userId, String product, Double amount) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.amount = amount;
    }

}
