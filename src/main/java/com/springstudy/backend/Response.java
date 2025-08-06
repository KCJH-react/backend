package com.springstudy.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Response<T> {
    private ErrorResponsev2 errorResponsev2;
    private T data;
}
