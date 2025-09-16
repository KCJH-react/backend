package com.springstudy.backend.Api.ChatGPT.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.ChatGPT.Model.Request.ChatGPTRequestDTO;
import com.springstudy.backend.Api.ChatGPT.Model.Response.ChatGPTResponseDTO;
import com.springstudy.backend.Api.ChatGPT.Model.ChatMessage;
import com.springstudy.backend.Api.ChatGPT.Model.Response.GPTChallengeResponse;
import com.springstudy.backend.Api.Repository.ChallengeRepository;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.User_UserCategoryRepository;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Error;
import com.springstudy.backend.Common.Responsev2.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatGPTService {
    private final WebClient webClient;
    private final String OPENAI_API_KEY;
    private final String OPENAI_MODEL;

    private final ChallengeRepository challengeRepository;
    private final User_UserCategoryRepository userUserCategoryRepository;

    public Challenge saveChallenge(ChallengeResponse response, Long userId) {
        Challenge challenge = response.toEntity(userId);
        return challengeRepository.save(challenge);
    }

    public ChatGPTService(ChallengeRepository challengeRepository, User_UserCategoryRepository userUserCategoryRepository) {
        this.challengeRepository = challengeRepository;
        this.userUserCategoryRepository = userUserCategoryRepository;
        Dotenv dotenv = Dotenv.load();
        this.OPENAI_API_KEY = dotenv.get("GPT");
        this.OPENAI_MODEL = dotenv.get("MODEL");

        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    
    
    //GPT 답변 생성
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

    //ResponseV3 타입으로 만들기
    public ResponseEntity<Response<GPTChallengeResponse>> makeChallengeResult(String systemPrompt, String userPrompt, Long userId) {
        try {
            ChallengeResponse challengeResponse = makeChallengeGPT(systemPrompt, userPrompt).block();
            if (challengeResponse == null) {
                return ResponseBuilder.<GPTChallengeResponse>create()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(null)
                        .errorResponsev2(Error.EXTERNAL_API_ERROR, "GPT 챌린지 생성중 에러 발생")
                        .build();
            }

            Challenge savedChallenge = saveChallenge(challengeResponse, userId);
            GPTChallengeResponse response = GPTChallengeResponse.fromEntity(savedChallenge);

            return ResponseBuilder.<GPTChallengeResponse>create()
                    .status(HttpStatus.OK)
                    .data(response)
                    .errorResponsev2(null, "챌린지 생성 성공")
                    .build();

        } catch (Exception e) {
            return ResponseBuilder.<GPTChallengeResponse>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(Error.PARSING_ERROR, "GPT 챌린지 파싱중 에러 발생")
                    .build();
        }
    }

    //미리 생성된 challenge 날짜들 확인하고 없으면 챌린지 생성하기 (이거 사용하면 됨)
    public ResponseEntity<Response<GPTChallengeResponse>> checkAndMakeChallengeResult(String systemPrompt, String userPrompt, Long userId) {
        try {
            Optional<Challenge> checkChallenge = getLatestTodayChallengeByUserId(userId);
            if (checkChallenge.isEmpty()) {
                return makeChallengeResult(systemPrompt, userPrompt, userId);
            }
            else {
                Challenge challengeEntity = checkChallenge.get();
                GPTChallengeResponse response = GPTChallengeResponse.fromEntity(challengeEntity);
                return ResponseBuilder.<GPTChallengeResponse>create()
                        .status(HttpStatus.OK)
                        .data(response)
                        .errorResponsev2(null, "챌린지 생성 성공")
                        .build();
            }
        } catch (Exception e) {
            return ResponseBuilder.<GPTChallengeResponse>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(Error.PARSING_ERROR, "최신 챌린지 확인 중 에러 발생")
                    .build();
        }
    }

    
    //userid 입력하면 해당 유저가 만든 가장 최신 챌린지 반환해줌(checkAndMakeChallengeResult 중간 삽입용 함수)
    public Optional<Challenge> getLatestTodayChallengeByUserId(Long userId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Challenge> todaysChallenges = challengeRepository.findTodaysChallengesByUserId(userId, startOfDay, endOfDay);

        if (todaysChallenges.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(todaysChallenges.get(0));
        }
    }
}
