package com.springstudy.backend.Api.PointExchange.Model.Request;

public class PointExchangeRequest {
    private String username;
    private int points;

    public PointExchangeRequest() {}

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}
