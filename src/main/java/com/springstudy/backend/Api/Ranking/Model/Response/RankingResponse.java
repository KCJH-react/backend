package com.springstudy.backend.Api.Ranking.Model.Response;

import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "랭킹 조회 응답")
public record RankingResponse (
        @NotNull
        @Schema(description = "성공 여부")
        ErrorCode code,

        @NotNull
        @Schema(description = "랭킹 목록")
        List<RankingData> rankingList
) {
    public record RankingData(
            @Schema(description = "유저 아이디")
            String userid,

            @Schema(description = "획득한 점수")
            Integer score,

            @Schema(description = "챌린지 클리어 횟수")
            Long successCount
    ) {}
}
