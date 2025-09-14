package com.springstudy.backend.Api.Challenge.Model.Response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
