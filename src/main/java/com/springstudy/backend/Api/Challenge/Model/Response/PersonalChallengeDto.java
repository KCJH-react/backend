package com.springstudy.backend.Api.Challenge.Model.Response;

import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import lombok.Getter;

@Getter
public class PersonalChallengeDto {
    private Long personalId;
    private String personalName;
    private String personalCompletionAction;
    private int personalDuration;
    private String personalCategory;

    public PersonalChallengeDto(PersonalChallenge entity) {
        this.personalId = entity.getPersonalId();
        this.personalName = entity.getPersonalChallengeName();
        this.personalCompletionAction = entity.getContent();
        this.personalDuration = entity.getDuration();
        this.personalCategory = entity.getPersonalChallengeCategory();
    }
}
