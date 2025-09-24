package com.springstudy.backend.Api.Challenge.Model.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalChallengeSaveRequest {
    private Long userid;
    private String personalName;
    private String personalCompletionAction;
    private Long personalDuration;
    private String personalCategory;
}