package com.springstudy.backend.Api.PointExchange.Controller;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.PointExchange.Service.PointExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointExchangeController {

    private final PointExchangeService pointExchangeService;

    //포인트 사용
    @PostMapping("/use")
    public ResponseEntity<PointExchangeResponse> usePoints(@RequestBody PointExchangeRequest request) {
        return ResponseEntity.ok(pointExchangeService.usePoints(request));
    }

    //포인트 적립
    @PostMapping("/earn")
    public ResponseEntity<PointExchangeResponse> earnPoints(@RequestBody PointExchangeRequest request) {
        return ResponseEntity.ok(pointExchangeService.earnPoints(request));
    }

    //사용자의 포인트 거래 내역 조회
    @GetMapping("/{username}/history")
    public ResponseEntity<List<PointExchangeResponse>> getUserPointHistory(@PathVariable String username) {
        return ResponseEntity.ok(pointExchangeService.getUserPointHistory(username));
    }
}
