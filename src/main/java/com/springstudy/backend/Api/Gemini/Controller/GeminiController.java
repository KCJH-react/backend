package com.springstudy.backend.Api.Gemini.Controller;

import com.springstudy.backend.Api.Gemini.Service.GeminiService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/gemini")
public class GeminiController {
    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message, Authentication auth) {
        return geminiService.chatGemini(message, auth);
    }
}
