package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Request;

import com.springstudy.backend.Api.Challenge.RandomChallenge.Model.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatGPTRequestDTO {
    private String model;
    private List<ChatMessage> messages;

    public ChatGPTRequestDTO(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
}
