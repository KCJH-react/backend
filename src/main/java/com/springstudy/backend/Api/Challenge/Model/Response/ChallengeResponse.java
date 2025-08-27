package com.springstudy.backend.Api.Challenge.Model.Response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {

    @JsonProperty("추천챌린지")
    private String content;

    @JsonProperty("난이도")
    private String difficult;

    @JsonProperty("달성 시간")
    @JsonAlias({"달성시간"})
    private String durationRaw;

    @JsonProperty("추천 이유")
    @JsonAlias({"추천이유"})
    private String reason;

    public int getDuration() {
        if (durationRaw == null) return 0;

        String numberStr = durationRaw.replaceAll("[^0-9]", "").trim();
        if (numberStr.isEmpty()) return 0;

        int value = Integer.parseInt(numberStr);

        if (durationRaw.contains("분")) {
            return value;
        } else if (durationRaw.contains("시간")) {
            return value * 60;
        } else if (durationRaw.contains("일")) {
            return value * 24 * 60;
        }
        return value;
    }
}
