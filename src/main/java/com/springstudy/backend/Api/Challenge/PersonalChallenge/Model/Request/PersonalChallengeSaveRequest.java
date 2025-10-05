package com.springstudy.backend.Api.Challenge.PersonalChallenge.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PersonalChallengeSaveRequest {
    private Long userid;
    private String personalName;
    private String personalCompletionAction;
    private int personalDuration;
    private String personalCategory;
}