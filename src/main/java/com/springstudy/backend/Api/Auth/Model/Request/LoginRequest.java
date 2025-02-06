package com.springstudy.backend.Api.Auth.Model.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "로그인 요청")
public record LoginRequest (
        @NotNull
        @NotBlank
        @Schema(description = "로그인 아이디")
        String userid,

        @NotNull
        @NotBlank
        @Schema(description = "회원가입 비밀번호")
        String password
){
}