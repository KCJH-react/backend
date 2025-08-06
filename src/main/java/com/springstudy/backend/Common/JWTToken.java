package com.springstudy.backend.Common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class JWTToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
