package com.springstudy.backend.Api.Auth.Model.DTOS;

import com.springstudy.backend.Api.Repository.Entity.Challenge;
import lombok.Data;

@Data
public class RandomChallengeDTO {
    private Long challengeId;
    private String content;
    private String difficulty;
    private int duration;
    private String reason;
    private Boolean success;

    public RandomChallengeDTO(Challenge challenge) {
        this.challengeId = challenge.getId();
        this.content = challenge.getContent();
        this.difficulty = challenge.getDifficulty();
        this.duration = challenge.getDuration();
        this.reason = challenge.getReason();
        this.success = challenge.getSuccess();
    }
}

// public class Challenge extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String difficulty;
//
//    private int duration;
//
//    private String reason;
//
//    private String content;
//
/// /    private Long userid;
//
//    private Boolean success = false;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//}