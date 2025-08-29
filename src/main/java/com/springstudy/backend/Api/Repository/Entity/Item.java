package com.springstudy.backend.Api.Repository.Entity;

import com.springstudy.backend.Common.Type.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    @Lob
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }
}
