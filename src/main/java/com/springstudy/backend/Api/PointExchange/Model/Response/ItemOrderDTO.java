package com.springstudy.backend.Api.PointExchange.Model.Response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemOrderDTO(
         Long id,
         String itemTitle,
         String useCode,
         LocalDateTime createdAt
) {
}

//     @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name="user_id", nullable=false)
//    private Long userId;           // users.id (zip의 UserRepository 사용)
//
//    @Column(name="item_id", nullable=false)
//    private Long itemId;           // items.id (zip의 Item.id = Long)
//
//    @Column(nullable=false)
//    private Integer quantity;
//
//    @Column(name="use_code", length=32, nullable=false, unique=true)
//    private String useCode;
//
//    @Column(name="created_at", nullable=false)
//    private LocalDateTime createdAt;