package com.springstudy.backend.Api.ChatGPT.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.ChatGPT.Model.Request.ChatGPTRequestDTO;
import com.springstudy.backend.Api.ChatGPT.Model.Response.ChatGPTResponseDTO;
import com.springstudy.backend.Api.ChatGPT.Model.ChatMessage;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.ErrorResponsev2;
import com.springstudy.backend.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatGPTService {
    private final WebClient webClient;
    private final String OPENAI_API_KEY;
    private final String OPENAI_MODEL;


    public ChatGPTService() {
        Dotenv dotenv = Dotenv.load();
        this.OPENAI_API_KEY = dotenv.get("GPT");
        this.OPENAI_MODEL = dotenv.get("MODEL");

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

//    public Mono<String> makeChallengeGPT(String systemPrompt, String userPrompt) {
//        ChatMessage systemMessage = new ChatMessage("system", systemPrompt);
//        ChatMessage userMessage = new ChatMessage("user", userPrompt);
//
//        ChatGPTRequestDTO request = new ChatGPTRequestDTO(
//                OPENAI_MODEL,
//                List.of(systemMessage, userMessage)
//        );
//        return webClient.post()
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(ChatGPTResponseDTO.class)
//                .map(response -> response.getChoices().get(0).getMessage().getContent());
//    }

    public Mono<ChallengeResponse> makeChallengeGPT(String systemPrompt, String userPrompt) {
        ChatMessage systemMessage = new ChatMessage("system", systemPrompt);
        ChatMessage userMessage = new ChatMessage("user", userPrompt);

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(
                OPENAI_MODEL,
                List.of(systemMessage, userMessage)
        );

        ObjectMapper objectMapper = new ObjectMapper();

        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponseDTO.class)
                .map(response -> {
                    String json = response.getChoices().get(0).getMessage().getContent();
                    try {
                        return objectMapper.readValue(json, ChallengeResponse.class);
                    } catch (Exception e) {
                        throw new RuntimeException("JSON 파싱 실패: " + e.getMessage(), e);
                    }
                });
    }

    public ResponseEntity<Response<ChallengeResponse>> makeChallengeResult(String systemPrompt, String userPrompt) {
        Response<ChallengeResponse> response = new Response<>(null, null);
        ChallengeResponse challengeResponse = makeChallengeGPT(systemPrompt, userPrompt).block();
        try {
            if (challengeResponse == null) {
                return ResponseBuilder.<ChallengeResponse>create()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(null)
                        .errorResponsev2(com.springstudy.backend.Error.EXTERNAL_API_ERROR, "GPT 챌린지 생성중 에러 발생")
                        .build();
            }
            return ResponseBuilder.<ChallengeResponse>create()
                    .status(HttpStatus.OK)
                    .data(challengeResponse)
                    .errorResponsev2(null, "챌린지 생성 성공")
                    .build();

        } catch (Exception e) {
            return ResponseBuilder.<ChallengeResponse>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(com.springstudy.backend.Error.PARSING_ERROR, "GPT 챌린지 파싱중 에러 발생")
                    .build();
        }
    }
}
