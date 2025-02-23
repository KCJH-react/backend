package com.springstudy.backend.Api.Auth.Model.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "이메일 인증번호 요청")
public record EmailRequest(
        @NotBlank
        @Schema(description = "이메일")
        String email
){

}
