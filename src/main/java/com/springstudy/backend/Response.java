package com.springstudy.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Response<T> {
    private ErrorResponsev2 errorResponsev2;
    private T data;
}
