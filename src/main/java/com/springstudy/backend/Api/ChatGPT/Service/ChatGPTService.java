package com.springstudy.backend.Api.ChatGPT.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springstudy.backend.Api.Auth.Model.UserDTO;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.ChatGPT.Model.Request.ChatGPTRequestDTO;
import com.springstudy.backend.Api.ChatGPT.Model.Response.ChatGPTResponseDTO;
import com.springstudy.backend.Api.ChatGPT.Model.ChatMessage;
import com.springstudy.backend.Api.ChatGPT.Model.Response.GPTChallengeResponse;
import com.springstudy.backend.Api.Repository.ChallengeRepository;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.Entity.UserCategory;
import com.springstudy.backend.Api.Repository.Entity.User_UserCategory;
import com.springstudy.backend.Api.Repository.UserCategoryRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Api.Repository.User_UserCategoryRepository;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Error;
import com.springstudy.backend.Common.Responsev2.Response;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ChatGPTService {
    private final WebClient webClient;
    private final String OPENAI_API_KEY;
    private final String OPENAI_MODEL;

    private final ChallengeRepository challengeRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final UserRepository userRepository;
    private final User_UserCategoryRepository userUserCategoryRepository;

    private final AuthService authService;

    public Challenge saveChallenge(ChallengeResponse response, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없습니다: " + userId));

        Challenge challenge = Challenge.builder()
                .content(response.getContent())
                .difficulty(response.getDifficult())
                .duration(response.getDuration())
                .reason(response.getReason())
                .success(false)
                .user(user)
                .build();
        return challengeRepository.save(challenge);
    }

    public ChatGPTService(ChallengeRepository challengeRepository, UserCategoryRepository userCategoryRepository, UserRepository userRepository, User_UserCategoryRepository userUserCategoryRepository, AuthService authService) {
        this.challengeRepository = challengeRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.userRepository = userRepository;
        this.userUserCategoryRepository = userUserCategoryRepository;
        this.authService = authService;
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

    //Response 타입으로 만들기
    public ResponseEntity<Response<GPTChallengeResponse>> makeChallengeResult(String systemPrompt, String userPrompt, Long userId) {
        try {
            ChallengeResponse challengeResponse = makeChallengeGPT(systemPrompt, userPrompt).block();
            Optional<User> userOptional = userRepository.findById(userId);
            if(userOptional.isEmpty()) {
                return ResponseBuilder.<GPTChallengeResponse>create()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(null)
                        .errorResponsev2(Error.EXTERNAL_API_ERROR, "(챌린지)유저 데이터 불러오기중 에러 발생")
                        .build();
            }
            if (challengeResponse == null) {
                return ResponseBuilder.<GPTChallengeResponse>create()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(null)
                        .errorResponsev2(Error.EXTERNAL_API_ERROR, "GPT 챌린지 생성중 에러 발생")
                        .build();
            }
            User user = userOptional.get();
            List<com.springstudy.backend.Common.Type.Challenge> category = new ArrayList<>();
            for(User_UserCategory challenge:
                    user.getUser_userCategoryList()){
                Optional<UserCategory> userCategoryOptional = userCategoryRepository.findById(challenge.getUserCategory().getId());
                if(userCategoryOptional.isPresent()){
                    //예외처리
                }
                category.add(userCategoryOptional.get().getChallenge());
            }
            Challenge savedChallenge = saveChallenge(challengeResponse, userId);
            GPTChallengeResponse combinedResponse = GPTChallengeResponse.builder()
                    // Challenge 정보
                    .id(savedChallenge.getId())
                    .content(savedChallenge.getContent())
                    .difficult(savedChallenge.getDifficulty())
                    .duration(savedChallenge.getDuration())
                    .reason(savedChallenge.getReason())
                    .createdAt(savedChallenge.getCreatedAt())
                    .success(savedChallenge.getSuccess())
                    // User 정보 추가
                    .username(user.getUsername())
                    .category(category)
                    .build();


            return ResponseBuilder.<GPTChallengeResponse>create()
                    .status(HttpStatus.OK)
                    .data(combinedResponse)
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
                Optional<User> userOptional = userRepository.findById(userId);
                if(userOptional.isEmpty()) {
                    return ResponseBuilder.<GPTChallengeResponse>create()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .errorResponsev2(Error.EXTERNAL_API_ERROR, "(기존 챌린지)유저 데이터 불러오기중 에러 발생")
                            .build();
                }
                User user = userOptional.get();
                List<com.springstudy.backend.Common.Type.Challenge> category = new ArrayList<>();
                for(User_UserCategory challenge:
                        user.getUser_userCategoryList()){
                    Optional<UserCategory> userCategoryOptional = userCategoryRepository.findById(challenge.getUserCategory().getId());
                    if(userCategoryOptional.isPresent()){
                        //예외처리
                    }
                    category.add(userCategoryOptional.get().getChallenge());
                }
//                GPTChallengeResponse response = GPTChallengeResponse.fromEntity(challengeEntity);
                GPTChallengeResponse combinedResponse = GPTChallengeResponse.builder()
                        // Challenge 정보
                        .id(challengeEntity.getId())
                        .content(challengeEntity.getContent())
                        .difficult(challengeEntity.getDifficulty())
                        .duration(challengeEntity.getDuration())
                        .reason(challengeEntity.getReason())
                        .createdAt(challengeEntity.getCreatedAt())
                        .success(challengeEntity.getSuccess())
                        // User 정보 추가
                        .username(user.getUsername())
                        .category(category)
                        .build();
                return ResponseBuilder.<GPTChallengeResponse>create()
                        .status(HttpStatus.OK)
                        .data(combinedResponse)
                        .errorResponsev2(null, "챌린지 생성 성공")
                        .build();
            }
        } catch (Exception e) {
            return ResponseBuilder.<GPTChallengeResponse>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(com.springstudy.backend.Common.Responsev2.Error.PARSING_ERROR, "최신 챌린지 확인 중 에러 발생")
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
