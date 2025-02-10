package com.springstudy.backend.Api.PrivateChallenge.Controller;

import com.springstudy.backend.Api.PrivateChallenge.Model.Request.PrivateChallengeRequest;
import com.springstudy.backend.Api.PrivateChallenge.Model.Response.PrivateChallengeResponse;
import com.springstudy.backend.Api.PrivateChallenge.Service.PrivateChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/privatechallenges")  // 기본 URL 설정
@RequiredArgsConstructor
public class PrivateChallengeController {

    private final PrivateChallengeService privateChallengeService;

    // 새로운 챌린지 생성 API
    @PostMapping("/create")
    public ResponseEntity<PrivateChallengeResponse> createChallenge(@RequestBody PrivateChallengeRequest request) {
        PrivateChallengeResponse response = privateChallengeService.createPrivateChallenge(request);
        return ResponseEntity.ok(response);
    }
}
