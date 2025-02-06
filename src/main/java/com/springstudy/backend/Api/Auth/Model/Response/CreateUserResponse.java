package com.springstudy.backend.Api.Auth.Model.Response;

import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회원 가입 요청")
public record CreateUserResponse (
        @NotNull
        @NotBlank
        @Schema(description = "성공 여부")
        ErrorCode code
){
}