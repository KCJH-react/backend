package com.springstudy.backend.Api.ChatGPT.Service;

import com.springstudy.backend.Api.ChatGPT.Model.Request.ChatGPTRequestDTO;
import com.springstudy.backend.Api.ChatGPT.Model.Response.ChatGPTResponseDTO;
import com.springstudy.backend.Api.ChatGPT.Model.ChatMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

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

    public Mono<String> makeChallengeGPT(String systemPrompt, String userPrompt) {
        ChatMessage systemMessage = new ChatMessage("system", systemPrompt);
        ChatMessage userMessage = new ChatMessage("user", userPrompt);

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(
                OPENAI_MODEL,
                List.of(systemMessage, userMessage)
        );

        return webClient.post()
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponseDTO.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent());
    }
}
