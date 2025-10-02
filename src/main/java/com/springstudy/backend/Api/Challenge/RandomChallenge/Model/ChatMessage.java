package com.springstudy.backend.Api.Challenge.RandomChallenge.Model;

//ChatGPT와 기본적인 통신용으로 사용중

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    private String role; // "user" 또는 "system" 또는 "assistant"
    private String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

}
