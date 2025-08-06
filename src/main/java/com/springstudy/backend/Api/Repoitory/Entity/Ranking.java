package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userid;

    private String rankType;

    private Long rankPosition;

    private Long point;

    private Long successCount;

    private Long updatedAt;

    private int score;

    public Ranking(String userid, String rankType, int score) {
        this.userid = userid;
        this.rankType = rankType;
        this.score = score;
    }
}
