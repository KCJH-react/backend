package com.springstudy.backend.Api.Repository.Entity;

import com.springstudy.backend.Common.Type.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter // ✅ preloadImg()에서 setUrl() 사용하므로 추가
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points;
    private String title;
    private String imgTitle;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    // ✅ 서비스에서 사용 중인 URL 컬럼 추가
    @Column(name = "url")
    private String url;
}
