package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Response;

import java.util.List;

//GPT에게 답변을 수신받는용도로 사용중입니다.

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
