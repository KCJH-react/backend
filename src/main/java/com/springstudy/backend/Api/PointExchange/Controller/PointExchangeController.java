package com.springstudy.backend.Api.PointExchange.Controller;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.PointExchange.Service.PointExchangeService;
import com.springstudy.backend.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointExchangeController {

    private final PointExchangeService pointExchangeService;

    @PostMapping("/use")
    public PointExchangeResponse usePoints(@RequestBody PointExchangeRequest request) {
        return pointExchangeService.usePoints(request);
    }

    @PostMapping("/earn")
    public PointExchangeResponse earnPoints(@RequestBody PointExchangeRequest request) {
        return pointExchangeService.earnPoints(request);
    }

    @GetMapping("/history/{username}")
    public List<PointExchangeResponse> getUserPointHistory(@PathVariable String username) {
        return pointExchangeService.getUserPointHistory(username);
    }

    @GetMapping("/getItems")
    public ResponseEntity<Response<List<ItemDTO>>> getItems() {
        return pointExchangeService.getItems();
    }

    // ✅ 주문 생성 API (todo1)
    @PostMapping("/orders")
    public ResponseEntity<Response<String>> createOrder(
            @RequestBody OrderCreateRequest req) {
        return pointExchangeService.createOrder(req.id(), req.itemId(), req.quantity());
    }

    // 요청 DTO (간단히 컨트롤러에 둠)
    public record OrderCreateRequest(Long id, Long itemId, int quantity) {}
}
