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

    @Column(name="user_id", nullable=false)
    private Long userId;           // users.id (zip의 UserRepository 사용)

    @Column(name="item_id", nullable=false)
    private Long itemId;           // items.id (zip의 Item.id = Long)

    @Column(nullable=false)
    private Integer quantity;

    @Column(name="use_code", length=32, nullable=false, unique=true)
    private String useCode;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

//    public enum Status { CREATED, COMPLETED, CANCELED, USED, REFUNDED }

//    @PrePersist
//    void prePersist() {
//        if (createdAt == null) createdAt = LocalDateTime.now();
//        if (status == null) status = Status.CREATED;
//        if (useCode == null) useCode = UUID.randomUUID().toString().replace("-", "").substring(0,16).toUpperCase();
//        if (quantity == null || quantity < 1) quantity = 1;
//    }
}
