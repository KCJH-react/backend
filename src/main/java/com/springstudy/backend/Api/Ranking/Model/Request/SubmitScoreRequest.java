package com.springstudy.backend.Api.Ranking.Model.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "점수 제출 요청")
public record SubmitScoreRequest (
        @NotNull
        @Schema(description = "획득한 점수")
        Integer score,

        @NotBlank
        @Schema(description = "랭크 타입 (예: TOTAL, WEEKLY)")
        String rankType
) { }
