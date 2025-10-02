package com.springstudy.backend.Api.Challenge.PersonalChallenge.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChallengeResponse {

    private String personalId;
    private String personalName;
    private String personalCompletionAction;
    private Long personalDuration;
    private String personalIcon;
    private String personalCategory;
}
