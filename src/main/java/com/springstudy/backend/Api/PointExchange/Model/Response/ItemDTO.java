package com.springstudy.backend.Api.PointExchange.Model.Response;

import com.springstudy.backend.Common.Type.ItemCategory;
import lombok.Builder;

@Builder
public record ItemDTO(
        Long id,
        String title,
        String Url,
        int points,
        ItemCategory itemCategory,
        long purchaseCount // ✅ 추가
) {}


//     @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String imgTitle;
//    private String title;
//
//    @Enumerated(EnumType.STRING)
//    private ItemCategory itemCategory;