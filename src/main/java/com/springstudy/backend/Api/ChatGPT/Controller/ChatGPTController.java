package com.springstudy.backend.Api.ChatGPT.Controller;


import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.ChatGPT.Prompt.PromptTemplate;
import com.springstudy.backend.Api.ChatGPT.Service.ChatGPTService;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {
    private final ChatGPTService chatService;

    public ChatGPTController(ChatGPTService chatService) {
        this.chatService = chatService;
    }

    //추후에 사용자의 기본 정보를 데이터베이스에서 가져와서 자동으로 프롬프트 완성하는 방식으로 바꿔야함
    @GetMapping("/challenge")
    public ResponseEntity<Response<ChallengeResponse>> getChallenge(
            @RequestParam List<String> categories,
            @RequestParam int age,
            @RequestParam String sex,
            @RequestParam int successRate
    ) {
        String system_prompt = PromptTemplate.generateSystemPrompt(categories);
        String user_prompt = PromptTemplate.generateUserPrompt(categories, age, sex, successRate);

        return chatService.makeChallengeResult(system_prompt, user_prompt);
    }

    @GetMapping("/test")
    public Mono<ChallengeResponse> testChallenge(
            @RequestParam List<String> categories,
            @RequestParam int age,
            @RequestParam String sex,
            @RequestParam int successRate
    ) {
        String system_prompt = PromptTemplate.generateSystemPrompt(categories);
        String user_prompt = PromptTemplate.generateUserPrompt(categories, age, sex, successRate);

        return chatService.makeChallengeGPT(system_prompt, user_prompt);
    }
}
