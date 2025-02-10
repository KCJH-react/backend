package com.springstudy.backend.Api.PrivateChallenge.Model.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChallengeRequest {

    @NotNull
    @NotBlank
    @Schema(description = "챌린지 카테고리", example = "Exercise")
    private String category;

    @NotNull
    @NotBlank
    @Schema(description = "챌린지 난이도", example = "Hard")
    private String difficulty;

    @NotNull
    @NotBlank
    @Schema(description = "챌린지 이름", example = "30분 러닝")
    private String name;

    @Schema(description = "챌린지 포인트", example = "50")
    private int points;
}
