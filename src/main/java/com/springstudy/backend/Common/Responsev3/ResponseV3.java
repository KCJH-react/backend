package com.springstudy.backend.Common.Responsev3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ResponseV3<T> {
    private T data;
    private LocalDateTime timestamp;
    private String message;
}