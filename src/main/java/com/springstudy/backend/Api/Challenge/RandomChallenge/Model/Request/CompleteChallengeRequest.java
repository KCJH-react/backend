package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CompleteChallengeRequest {
    private Long userId;
    private Long challengeId;
}
