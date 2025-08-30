package com.springstudy.backend.Api.Challenge.Controller;

import com.springstudy.backend.Api.Challenge.Service.ChallengeService;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Operation(summary = "챌린지 완료 처리", description = "사용자가 특정 챌린지를 완료하면 포인트를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 완료 성공"),
            @ApiResponse(responseCode = "500", description = "챌린지 완료 실패")
    })
    @PostMapping("/complete")
    public ResponseEntity<String> completeChallenge(
            @Parameter(description = "사용자의 ID") @RequestParam("userId") Long userid,
            @Parameter(description = "완료한 챌린지의 ID") @RequestParam("challengeId") Long challengeId) {

        // 챌린지 완료 처리
        boolean isCompleted = challengeService.completeChallenge(userid, challengeId);

        if (isCompleted) {
            return ResponseEntity.ok("챌린지 완료 성공!");
        } else {
            return ResponseEntity.status(500).body("챌린지 완료 실패");
        }
    }
    @GetMapping("/getsuccess")
    public ResponseEntity<List<Challenge>> getSuccessChallenges(@RequestParam Long userId) {
        List<Challenge> challenges = challengeService.getSuccessChallenges(userId);
        return ResponseEntity.ok(challenges);
    }
}
