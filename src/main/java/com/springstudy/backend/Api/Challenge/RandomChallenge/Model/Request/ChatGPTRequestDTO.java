package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Request;

import com.springstudy.backend.Api.Challenge.RandomChallenge.Model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class ChatGPTRequestDTO {
    private String model;
    private List<ChatMessage> messages;

    public ChatGPTRequestDTO(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }
}
