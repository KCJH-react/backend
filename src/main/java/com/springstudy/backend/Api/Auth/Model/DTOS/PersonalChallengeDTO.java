package com.springstudy.backend.Api.Auth.Model.DTOS;

import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import lombok.Data;

@Data
public class PersonalChallengeDTO {
    private Long personalId;
    private String personalName;
    private Long personalDuration;
    private String personalCategory;
    private String personalCompletionAction;

    public PersonalChallengeDTO(PersonalChallenge pc) {
        this.personalId = pc.getPersonalId();
        this.personalName = pc.getPersonalName();
        this.personalDuration = pc.getPersonalDuration();
        this.personalCategory = pc.getPersonalCategory();
        this.personalCompletionAction = pc.getPersonalCompletionAction();
    }
}