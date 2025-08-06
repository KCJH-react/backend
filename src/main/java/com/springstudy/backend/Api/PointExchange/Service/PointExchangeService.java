package com.springstudy.backend.Api.PointExchange.Service;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.Repository.Entity.PointExchange;
import com.springstudy.backend.Api.Repository.PointExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointExchangeService {

    private final PointExchangeRepository pointExchangeRepository;

    //포인트 사용
    public PointExchangeResponse usePoints(PointExchangeRequest request) {
        PointExchange exchange = PointExchange.builder()
                .username(request.getUsername())
                .points(request.getPoints())
                .exchangeType("USE")
                .build();

        PointExchange savedExchange = pointExchangeRepository.save(exchange);
        return convertToResponse(savedExchange);
    }

    //포인트 적립
    public PointExchangeResponse earnPoints(PointExchangeRequest request) {
        PointExchange exchange = PointExchange.builder()
                .username(request.getUsername())
                .points(request.getPoints())
                .exchangeType("EARN")
                .build();

        PointExchange savedExchange = pointExchangeRepository.save(exchange);
        return convertToResponse(savedExchange);
    }

    //포인트 거래 내역 조회
    public List<PointExchangeResponse> getUserPointHistory(String username) {
        return pointExchangeRepository.findByUsername(username)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PointExchangeResponse convertToResponse(PointExchange exchange) {
        return PointExchangeResponse.builder()
                .username(exchange.getUsername())
                .points(exchange.getPoints())
                .exchangeType(exchange.getExchangeType())
                .exchangeDate(exchange.getExchangeDate())
                .build();
    }
}
