package com.springstudy.backend.Api.PointExchange.Model.Response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class PointExchangeResponse {
    private String username;
    private int points;
    private String exchangeType;
    private LocalDateTime exchangeDate;
}
