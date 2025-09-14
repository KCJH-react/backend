package com.springstudy.backend.Api.Ranking.Controller;

import com.springstudy.backend.Api.Ranking.Model.Request.SubmitScoreRequest;
import com.springstudy.backend.Api.Ranking.Model.Response.RankingResponse;
import com.springstudy.backend.Api.Ranking.Model.Response.SubmitScoreResponse;
import com.springstudy.backend.Api.Ranking.Service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingControllerV1 {
    private final RankingService rankingService;

    // 인증 사용자의 점수 제출 (req에 score, rankType 포함)
    @PostMapping("/submitScore")
    public SubmitScoreResponse submitScore(@RequestBody SubmitScoreRequest submitScoreRequest) {
        return rankingService.submitScore(submitScoreRequest);
    }

    // 타입별 Top10 (예: /api/v1/ranking/topRanks?type=TOTAL)
    @GetMapping("/topRanks")
    public RankingResponse getTopRanks(@RequestParam("type") String rankType) {
        return rankingService.getTopRanks(rankType);
    }

    // 선택: 내 랭킹 단건 조회 (없으면 빈 리스트 반환)
    // GET /api/v1/ranking/me?type=TOTAL
    @GetMapping("/me")
    @Operation(
            summary = "내 랭킹 조회",
            description = "로그인된 사용자의 특정 랭크 타입 점수를 반환합니다",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "내 랭킹 조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "랭킹 있음",
                                                    value = """
                        {
                          "code": {
                            "code": 0,
                            "message": "SUCCESS"
                          },
                          "rankingList": [
                            {
                              "userid": "myUsername",
                              "score": 1500
                            }
                          ]
                        }
                        """
                                            ),
                                            @ExampleObject(
                                                    name = "랭킹 없음",
                                                    value = """
                        {
                          "code": {
                            "code": 0,
                            "message": "SUCCESS"
                          },
                          "rankingList": []
                        }
                        """
                                            )
                                    }
                            )
                    )
            }
    )
    public RankingResponse getMyRank(@RequestParam("type") String rankType) {
        return rankingService.getMyRank(rankType);
    }
}
