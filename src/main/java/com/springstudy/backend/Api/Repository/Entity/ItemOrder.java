package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "item_orders",
        uniqueConstraints = @UniqueConstraint(columnNames = "use_code"),
        indexes = {
                @Index(name = "idx_user_item", columnList = "user_id,item_id"),
                @Index(name = "idx_item", columnList = "item_id"),
                @Index(name = "idx_status", columnList = "status")
        })
public class ItemOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ JPA 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_orders_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_item_orders_item"))
    private Item item;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "status", length = 20, nullable = false)
    private String status;   // 주문 상태 (예: CREATED, USED 등)

    @Column(name = "use_code", length = 32, nullable = false, unique = true)
    private String useCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (useCode == null) {
            useCode = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, 16)
                    .toUpperCase();
        }
        if (quantity == null || quantity < 1) quantity = 1;
        if (status == null) status = "CREATED";
    }
}

//    public enum Status { CREATED, COMPLETED, CANCELED, USED, REFUNDED }

//    @PrePersist
//    void prePersist() {
//        if (createdAt == null) createdAt = LocalDateTime.now();
//        if (status == null) status = Status.CREATED;
//        if (useCode == null) useCode = UUID.randomUUID().toString().replace("-", "").substring(0,16).toUpperCase();
//        if (quantity == null || quantity < 1) quantity = 1;
//    }

