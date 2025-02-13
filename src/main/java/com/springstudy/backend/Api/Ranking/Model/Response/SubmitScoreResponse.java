package com.springstudy.backend.Api.Ranking.Model.Response;

import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "점수 제출 응답")
public record SubmitScoreResponse (
        @NotNull
        @NotBlank
        @Schema(description = "성공 여부")
        ErrorCode code
) {
}
