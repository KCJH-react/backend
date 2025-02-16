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

    // gemini 기능 에러
    TEMPLATE_PARSEERROR(-6,"TEMPLATE_PARSEERROR"),
    TEMPLATE_NAME_ERROR(-7,"TEMPLATE_NAME_ERROR"),
    TEMPLATE_VARIABLE_ERROR(-8,"TEMPLATE_VARIABLE_ERROR"),
    TEMPLATE_IO_ERROR(-9,"TEMPLATE_IO_ERROR"),
    RESPONSE_NULL(-10,"RESPONSE_NULL"),
    RESTTEMPLATE_REQUEST_ERROR (-10, "RESTTEMPLATE_REQUEST_ERROR");

    private final Integer code;
    private final String message;
}
