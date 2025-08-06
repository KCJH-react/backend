package com.springstudy.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class ErrorResponsev2 {
    private Error code;
    private String message;
}
