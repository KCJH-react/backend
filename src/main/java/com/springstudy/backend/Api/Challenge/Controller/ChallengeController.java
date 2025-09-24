package com.springstudy.backend.Api.Challenge.Controller;

import com.springstudy.backend.Api.Challenge.Model.Request.CompleteChallengeRequest;
import com.springstudy.backend.Api.Challenge.Model.Request.PersonalChallengeSaveRequest;
import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.Challenge.Model.Response.PersonalChallengeDto;
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
import java.util.Optional;

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
            @RequestBody PersonalChallengeSaveRequest request
    ) {
        return challengeService.savePersonalChallengeResult(
                request.getUserid(),
                request.getPersonalName(),
                request.getPersonalCompletionAction(),
                request.getPersonalDuration(),
                request.getPersonalCategory()
        );
    }

    @GetMapping("/personal")
    public ResponseEntity<Response<List<PersonalChallengeDto>>> getAllPersonalChallenges(
            @RequestParam Long userId
    ) {
        List<PersonalChallengeDto> personalChallenges = challengeService.getAllPersonalChallengesByUserId(userId);

        return ResponseBuilder.<List<PersonalChallengeDto>>create()
                .status(HttpStatus.OK)
                .data(personalChallenges)
                .errorResponsev2(null, "개인 챌린지 목록 조회 성공")
                .build();
    }

    @GetMapping("/personal/{challengeId}")
    public ResponseEntity<Response<PersonalChallengeDto>> getPersonalChallengeById(
            @PathVariable Long challengeId
    ) {
        Optional<PersonalChallenge> challengeOptional = challengeService.getPersonalChallengeById(challengeId);

        return challengeOptional
                .map(challenge -> {
                    PersonalChallengeDto dto = new PersonalChallengeDto(challenge);
                    return ResponseBuilder.<PersonalChallengeDto>create()
                            .status(HttpStatus.OK)
                            .data(dto)
                            .build();
                })
                .orElseGet(() -> ResponseBuilder.<PersonalChallengeDto>create()
                        .status(HttpStatus.NOT_FOUND) // ID에 해당하는 데이터가 없을 때
                        .errorResponsev2(null, "해당 ID의 개인 챌린지를 찾을 수 없습니다.")
                        .data(null)
                        .build());
    }


    //개인 챌린지용
    @PostMapping("/personalComplete")
    public ResponseEntity<Response<Boolean>> personalComplete(
            @RequestParam Long userid) {

        // 챌린지 완료 처리
        boolean isCompleted = challengeService.addPointsToUser(userid);

        if (isCompleted) {
            return ResponseBuilder.<Boolean>create()
                    .status(HttpStatus.OK)
                    .data(true)
                    .errorResponsev2(null, "개인 챌린지 성공")
                    .build();
        } else {
            return ResponseBuilder.<Boolean>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(false)
                    .errorResponsev2(null, "개인 챌린지 실패")
                    .build();
        }
    }
}
