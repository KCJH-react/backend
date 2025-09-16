package com.springstudy.backend.Common.Responsev3;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ResponseV3<T> {
    private T data;
    private LocalDateTime timestamp;
    private String message;
}