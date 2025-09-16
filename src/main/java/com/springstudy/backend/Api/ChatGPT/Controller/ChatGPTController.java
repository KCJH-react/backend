package com.springstudy.backend.Api.ChatGPT.Controller;


import com.springstudy.backend.Api.Auth.Model.UserDTO;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.ChatGPT.Model.Response.GPTChallengeResponse;
import com.springstudy.backend.Api.ChatGPT.Prompt.PromptTemplate;
import com.springstudy.backend.Api.ChatGPT.Service.ChatGPTService;
import com.springstudy.backend.Common.Responsev2.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {
    private final ChatGPTService chatService;
    private final AuthService authService;

    public ChatGPTController(ChatGPTService chatService, AuthService authService) {
        this.chatService = chatService;
        this.authService = authService;
    }

    //성공률 계산 만들어야함
    @GetMapping("/challenge")
    public ResponseEntity<Response<GPTChallengeResponse>> getChallenge(
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

        return chatService.checkAndMakeChallengeResult(system_prompt, user_prompt, userid);
    }

    @GetMapping("/test")
    public ResponseEntity<Response<GPTChallengeResponse>> testChallenge(
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

        return chatService.makeChallengeResult(system_prompt, user_prompt, userid);
    }
}
