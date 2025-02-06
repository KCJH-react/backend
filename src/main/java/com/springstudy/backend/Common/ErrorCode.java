package com.springstudy.backend.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements CodeInterface{
    SUCCESS(0,"SUCCESS"),
    USER_ALREADY_EXISTS(-1, "USER_ALREADY_EXISTS"),
    USER_CREATE_FAILED(-2,"USER_CREATE_FAILED"),
    NOT_EXIST_USER(-3, "NOT_EXIST_USER"),
    MISMATCH_PASSWORD(-4, "MISMATCH_PASSWORD");
    private final Integer code;
    private final String message;
}
