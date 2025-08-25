package com.springstudy.backend;

public enum Error {
    BAD_REQUEST,            // 400
    UNAUTHORIZED,           // 401
    FORBIDDEN,              // 403
    NOT_FOUND,              // 404
    CONFLICT,               // 409
    INTERNAL_SERVER_ERROR,  // 500
    VALIDATION_ERROR,       // 입력값 검증 실패
    DATABASE_ERROR,         // DB 처리 실패
    BUSINESS_LOGIC_ERROR,   // 비즈니스 예외
    EXTERNAL_API_ERROR,     // 외부 API 호출 실패
    TIMEOUT,                // 시간 초과
    UNKNOWN_ERROR,           // 알 수 없는 에러
    OK
}
