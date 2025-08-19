package com.springstudy.backend.Api.Auth.Model.Request;

import com.springstudy.backend.Common.Type.UserInfoType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원정보 변경")
public record UpdateRequest(

        @Schema(description = "변경할 정보")
        UserInfoType type,

        @Schema(description = "확인용 비번")
        String password,

        @Schema(description = "변경할 내용 (선호 챌린지 ','로 연결,  생년월일 'yyyy-MM-dd' 형식, 성별 '남자' 혹은 '여자'")
        String content

) {
}
