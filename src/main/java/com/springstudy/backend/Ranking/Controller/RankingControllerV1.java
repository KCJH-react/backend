package com.springstudy.backend.Ranking.Controller;

import com.springstudy.backend.Ranking.Model.Request.SubmitScoreRequest;
import com.springstudy.backend.Ranking.Model.Response.RankingResponse;
import com.springstudy.backend.Ranking.Model.Response.SubmitScoreResponse;
import com.springstudy.backend.Ranking.Service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ranking")
@RequiredArgsConstructor
public class RankingControllerV1 {
    private final RankingService rankingService;

    @PostMapping("/submitScore")
    public SubmitScoreResponse submitScore(@RequestBody SubmitScoreRequest submitScoreRequest) {
        return rankingService.submitScore(submitScoreRequest);
    }

    @GetMapping("/topRanks")
    public RankingResponse getTopRanks() {
        return rankingService.getTopRanks();
    }
}
