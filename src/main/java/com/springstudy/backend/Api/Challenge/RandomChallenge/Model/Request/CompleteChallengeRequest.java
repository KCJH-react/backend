package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteChallengeRequest {
    private Long userId;
    private Long challengeId;
}
