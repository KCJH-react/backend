package com.springstudy.backend.Common.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface{
    SUCCESS(0,"SUCCESS"),
    USER_ALREADY_EXISTS(-1, "USER_ALREADY_EXISTS"),
    USER_CREATE_FAILED(-2,"USER_CREATE_FAILED"),
    NOT_EXIST_USER(-3, "NOT_EXIST_USER"),
    MISMATCH_PASSWORD(-4, "MISMATCH_PASSWORD"),
    FAILURE(-5,"FAILURE"),
    NOT_LOGIN(-6,"NOT_LOGIN"),
    NUMBER_FORMAT_ERROR(-7,"NUMBER_FORMAT_ERROR"),
    NOT_EXIST_CHALLENGE(-8,"NOT_EXIST_CHALLENGE"),
    ERROR_DATA_ACCESS(-9,"ERROR_DATA_ACCESS"),
    NOT_DELETE_CHALLENGE(-10,"NOT_DELETE_CHALLENGE"),
    ERROR_REDIS_ACCESS(-11,"ERROR_REDIS_ACCESS"),

    // gemini 기능 에러
    TEMPLATE_PARSEERROR(-6,"TEMPLATE_PARSEERROR"),
    TEMPLATE_NAME_ERROR(-7,"TEMPLATE_NAME_ERROR"),
    TEMPLATE_VARIABLE_ERROR(-8,"TEMPLATE_VARIABLE_ERROR"),
    TEMPLATE_IO_ERROR(-9,"TEMPLATE_IO_ERROR"),
    RESPONSE_NULL(-10,"RESPONSE_NULL"),
    RESTTEMPLATE_REQUEST_ERROR (-10, "RESTTEMPLATE_REQUEST_ERROR"),

    JWT_CREATE_ERROR(-11, "JWT_CREATE_ERROR"),
    AUTH_SAVE_ERROR(-12, "AUTH_SAVE_ERROR"),
    SIGNATURE_EXCEPTION(-13, "SIGNATURE_EXCEPTION"),
    JWT_EXPIRATE_PASSED(-14, "JWT_EXPIRATE_PASSED"),
    JWT_ACCESS_DENIED(-15, "JWT_ACCESS_DENIED");

    private final Integer code;
    private final String message;
}
