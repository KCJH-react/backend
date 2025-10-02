package com.springstudy.backend.Api.Challenge.PersonalChallenge.Controller;

import com.springstudy.backend.Api.Challenge.PersonalChallenge.Model.Request.PersonalChallengeSaveRequest;
import com.springstudy.backend.Api.Challenge.PersonalChallenge.Model.Response.PersonalChallengeDto;
import com.springstudy.backend.Api.Challenge.PersonalChallenge.Service.PersonalChallengeService;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personalChallenge")
public class PersonalChallengeController {

    @Autowired
    private PersonalChallengeService personalChallengeService;

    @GetMapping("/getsuccess")
    public ResponseEntity<List<Challenge>> getSuccessChallenges(@RequestParam Long userId) {
        List<Challenge> challenges = personalChallengeService.getSuccessChallenges(userId);
        return ResponseEntity.ok(challenges);
    }

    @PostMapping("/saveChallenge")
    public ResponseEntity<Response<PersonalChallenge>> savePersonalChallenge(
            @RequestBody PersonalChallengeSaveRequest request
    ) {
        return personalChallengeService.savePersonalChallengeResult(
                request.getUserid(),
                request.getPersonalName(),
                request.getPersonalCompletionAction(),
                request.getPersonalDuration(),
                request.getPersonalCategory()
        );
    }

    @GetMapping("/getPersonal")
    public ResponseEntity<Response<List<PersonalChallengeDto>>> getAllPersonalChallenges(
            @RequestParam Long userId
    ) {
        List<PersonalChallengeDto> personalChallenges = personalChallengeService.getAllPersonalChallengesByUserId(userId);

        return ResponseBuilder.<List<PersonalChallengeDto>>create()
                .status(HttpStatus.OK)
                .data(personalChallenges)
                .errorResponsev2(null, "개인 챌린지 목록 조회 성공")
                .build();
    }

    @GetMapping("/getPersonal/{challengeId}")
    public ResponseEntity<Response<PersonalChallengeDto>> getPersonalChallengeById(
            @PathVariable Long challengeId
    ) {
        Optional<PersonalChallenge> challengeOptional = personalChallengeService.getPersonalChallengeById(challengeId);

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
        boolean isCompleted = personalChallengeService.addPointsToUser(userid);

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
