package com.springstudy.backend.Api.Auth.Model.DTOS;

import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import lombok.Data;

@Data
public class PersonalChallengeDTO {
    private Long personalId;
    private String personalName;
    private int personalDuration;
    private String personalCategory;
    private String personalCompletionAction;

    public PersonalChallengeDTO(PersonalChallenge pc) {
        this.personalId = pc.getPersonalId();
        this.personalName = pc.getPersonalChallengeName();
        this.personalDuration = pc.getDuration();
        this.personalCategory = pc.getPersonalChallengeCategory();
        this.personalCompletionAction = pc.getContent();
    }
}