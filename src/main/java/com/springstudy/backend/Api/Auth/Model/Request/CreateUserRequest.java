package com.springstudy.backend.Api.Auth.Model.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springstudy.backend.Common.Type.Challenge;
import com.springstudy.backend.Common.Type.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "회원 가입 요청")
public record CreateUserRequest (

        @NotBlank
        @Schema(description = "회원가입 유저 이름")
        String username,

        @NotBlank
        @Schema(description = "회원가입 이메일")
        String email,

        @NotBlank
        @Schema(description = "회원가입 비밀번호")
        String password,

        @Schema(description = "성별")
        Sex sex,

        @Schema(description = "생년월일")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
        String birthday,

        @Schema(description = "목표")
        String goal,

        @Schema(description = "선호챌린지")
        Challenge[] preferredChallenge,

        @Schema(description = "이미지 url")
        @Lob
        String imgUrl
){
}