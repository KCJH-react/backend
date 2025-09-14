package com.springstudy.backend.Api.Repository.Entity;

import com.springstudy.backend.Common.Type.ItemCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(columnDefinition = "LONGTEXT")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }
}