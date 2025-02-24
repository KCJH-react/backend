package com.springstudy.backend.Api.PointExchange.Model.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointExchangeRequest {
    private String username;
    private int points;
}