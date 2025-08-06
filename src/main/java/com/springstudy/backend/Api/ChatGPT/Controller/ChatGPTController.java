package com.springstudy.backend.Api.ChatGPT.Controller;


import com.springstudy.backend.Api.ChatGPT.Service.ChatGPTService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/chat")
public class ChatGPTController {
    private final ChatGPTService chatService;

    public ChatGPTController(ChatGPTService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public Mono<String> chat(@RequestParam String prompt) {
        return chatService.getCompletion(prompt);
    }
}
