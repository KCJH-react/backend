package com.springstudy.backend.Api.PointExchange.Model.Response;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PointExchangeResponse {
    private String username;
    private int points;
    private String exchangeType;
    private LocalDateTime exchangeDate;

    public PointExchangeResponse() {}

    public PointExchangeResponse(String username, int points, String exchangeType, LocalDateTime exchangeDate) {
        this.username = username;
        this.points = points;
        this.exchangeType = exchangeType;
        this.exchangeDate = exchangeDate;
    }

    // ✅ 빌더 대신 사용하는 정적 팩토리
    public static PointExchangeResponse of(String username, int points, String exchangeType, LocalDateTime exchangeDate) {
        return new PointExchangeResponse(username, points, exchangeType, exchangeDate);
    }
}
