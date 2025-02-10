package com.springstudy.backend.Api.PrivateChallenge.Model.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class PrivateChallengeResponse {

    @Schema(description = "챌린지 ID", example = "1")
    private Long id;

    @Schema(description = "챌린지 카테고리", example = "Exercise")
    private String category;

    @Schema(description = "챌린지 난이도", example = "Hard")
    private String difficulty;

    @Schema(description = "챌린지 이름", example = "30분 러닝")
    private String name;

    @Schema(description = "획득 포인트", example = "50")
    private int points;
}
