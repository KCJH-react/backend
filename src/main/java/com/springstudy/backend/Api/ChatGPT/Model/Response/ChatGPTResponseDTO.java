package com.springstudy.backend.Api.ChatGPT.Model.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ChatGPTResponseDTO {
    private List<Choice> choices;

    public static class Choice {
        private Message message;
        public Message getMessage() { return message; }

        public static class Message {
            private String role;
            private String content;
            public String getContent() { return content; }
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }
}
