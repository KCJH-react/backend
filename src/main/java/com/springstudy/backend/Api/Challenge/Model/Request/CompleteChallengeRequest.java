package com.springstudy.backend.Api.Challenge.Model.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteChallengeRequest {
    private Long userId;
    private Long challengeId;
}
