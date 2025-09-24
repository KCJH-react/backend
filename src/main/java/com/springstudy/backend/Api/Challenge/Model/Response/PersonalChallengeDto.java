package com.springstudy.backend.Api.Challenge.Model.Response;

import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import lombok.Getter;

@Getter
public class PersonalChallengeDto {
    private Long personalId;
    private String personalName;
    private String personalCompletionAction;
    private Long personalDuration;
    private String personalCategory;

    public PersonalChallengeDto(PersonalChallenge entity) {
        this.personalId = entity.getPersonalId();
        this.personalName = entity.getPersonalName();
        this.personalCompletionAction = entity.getPersonalCompletionAction();
        this.personalDuration = entity.getPersonalDuration();
        this.personalCategory = entity.getPersonalCategory();
    }
}
