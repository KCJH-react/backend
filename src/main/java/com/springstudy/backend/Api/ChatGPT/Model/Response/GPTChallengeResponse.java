package com.springstudy.backend.Api.ChatGPT.Model.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GPTChallengeResponse {

    private Long id;
    private String content;
    private String difficult;
    private int duration;
    private String reason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Boolean success;

    private String username;
    private List<com.springstudy.backend.Common.Type.Challenge> category;

    public static GPTChallengeResponse fromEntity(Challenge challenge) {
        if (challenge == null) {
            return null;
        }
        return GPTChallengeResponse.builder()
                .content(challenge.getContent())
                .difficult(challenge.getDifficulty())
                .duration(challenge.getDuration())
                .reason(challenge.getReason())
                .createdAt(challenge.getCreatedAt())
                .success(challenge.getSuccess())
                .build();
    }

}
