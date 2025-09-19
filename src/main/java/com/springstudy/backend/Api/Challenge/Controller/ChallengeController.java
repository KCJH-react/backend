package com.springstudy.backend.Api.Challenge.Controller;

import com.springstudy.backend.Api.Challenge.Model.Request.CompleteChallengeRequest;
import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.Challenge.Service.ChallengeService;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Response<Boolean>> completeChallenge(
            @RequestBody CompleteChallengeRequest request) {

        // 챌린지 완료 처리
        boolean isCompleted = challengeService.completeChallenge(request.getUserId(), request.getChallengeId());

        if (isCompleted) {
            return ResponseBuilder.<Boolean>create()
                    .status(HttpStatus.OK)
                    .data(true)
                    .errorResponsev2(null, "챌린지 완료 성공!")
                    .build();
        } else {
            return ResponseBuilder.<Boolean>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(false)
                    .errorResponsev2(null, "챌린지 완료 실패")
                    .build();
        }
    }
    @GetMapping("/getsuccess")
    public ResponseEntity<List<Challenge>> getSuccessChallenges(@RequestParam Long userId) {
        List<Challenge> challenges = challengeService.getSuccessChallenges(userId);
        return ResponseEntity.ok(challenges);
    }

    @PostMapping("/personalChallenge/saveChallenge")
    public ResponseEntity<Response<PersonalChallenge>> savePersonalChallenge(
            @RequestParam Long userId,
            @RequestParam String personalName,
            @RequestParam String personalCompletionAction,
            @RequestParam Long personalDuration,
            @RequestParam String personalIcon,
            @RequestParam String personalCategory
    ) {
        return challengeService.savePersonalChallengeResult(userId, personalName, personalCompletionAction, personalDuration, personalIcon, personalCategory);
    }
}
