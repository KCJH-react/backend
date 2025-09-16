package com.springstudy.backend.Common.Responsev2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Response<T> {
    private ErrorResponsev2 errorResponsev2;
    private T data;
}
