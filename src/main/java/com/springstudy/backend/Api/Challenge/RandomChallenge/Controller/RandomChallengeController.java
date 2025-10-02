package com.springstudy.backend.Api.Challenge.RandomChallenge.Controller;


import com.springstudy.backend.Api.Auth.Model.UserDTO;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Request.CompleteChallengeRequest;
import com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Response.RandomChallengeResponse;
import com.springstudy.backend.Api.Challenge.RandomChallenge.Prompt.PromptTemplate;
import com.springstudy.backend.Api.Challenge.RandomChallenge.Service.RandomChallengeService;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/randomChallenge")
public class RandomChallengeController {
    private final AuthService authService;
    private final RandomChallengeService randomChallengeService;

    public RandomChallengeController(AuthService authService, RandomChallengeService randomChallengeService) {
        this.authService = authService;
        this.randomChallengeService = randomChallengeService;
    }

    @GetMapping("/challenge")
    public ResponseEntity<Response<RandomChallengeResponse>> getChallenge(
            @RequestParam Long userid
    ) {
        ResponseEntity<Response<UserDTO>> userResponse = authService.get(userid);
        UserDTO userDTO = userResponse.getBody().getData();

        List<String> category = userDTO.getCategory()
                .stream()
                .map(Enum::name)
                .toList();

        int age = userDTO.getAge();
        String sex = userDTO.getSex().name();

        String system_prompt = PromptTemplate.generateSystemPrompt(category);
        String user_prompt = PromptTemplate.generateUserPrompt(category, age, sex, 80);

        return randomChallengeService.checkAndMakeChallengeResult(system_prompt, user_prompt, userid);
    }

    @GetMapping("/reroll")
    public ResponseEntity<Response<RandomChallengeResponse>> testChallenge(
            @RequestParam Long userid
    ) {
        ResponseEntity<Response<UserDTO>> userResponse = authService.get(userid);
        UserDTO userDTO = userResponse.getBody().getData();

        List<String> category = userDTO.getCategory()
                .stream()
                .map(Enum::name)
                .toList();

        int age = userDTO.getAge();
        String sex = userDTO.getSex().name();

        String system_prompt = PromptTemplate.generateSystemPrompt(category);
        String user_prompt = PromptTemplate.generateUserPrompt(category, age, sex, 80);

        return randomChallengeService.makeChallengeResult(system_prompt, user_prompt, userid);
    }

    @Operation(summary = "챌린지 완료 처리", description = "사용자가 특정 챌린지를 완료하면 포인트를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "챌린지 완료 성공"),
            @ApiResponse(responseCode = "500", description = "챌린지 완료 실패")
    })
    @PostMapping("/complete")
    public ResponseEntity<Response<Boolean>> completeChallenge(
            @RequestBody CompleteChallengeRequest request) {

        // 챌린지 완료 처리
        boolean isCompleted = randomChallengeService.completeChallenge(request.getUserId(), request.getChallengeId());

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
}
