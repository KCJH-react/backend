package com.springstudy.backend.Api.Challenge.RandomChallenge.Model.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

//실질적으로 RandomChallengeService에서 프론트에 전달되는 형식으로 사용중입니다.

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomChallengeResponse {

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
}
