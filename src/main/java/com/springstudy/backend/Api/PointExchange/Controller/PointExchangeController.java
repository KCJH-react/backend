package com.springstudy.backend.Api.PointExchange.Controller;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.PointExchange.Service.PointExchangeService;
import com.springstudy.backend.Api.Repository.ItemRepository;
import com.springstudy.backend.Common.FirebaseService;
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

    private final FirebaseService firebaseService;

    @GetMapping("/firebase")
    public ResponseEntity<String> getFileUrl(String fileName) {
        String url = firebaseService.getFileUrl(fileName);
        if (url == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/getItems")
    public ResponseEntity<Response<List<ItemDTO>>> getItems() {
        return pointExchangeService.getItems();
    }
    
    //todo 1: item id와 user id를 속성으로 갖는 테이블 만들어서 주문 데이터를 저장할 것.
    // 사용자가 아이템을 선택하고 주문을 요청하면 생성되도록 + 이 테이블에 추가로 거래시간, 사용코드, 등 추가

    //todo 2: ItemDTO에 구매건수를 속성을 추가하고 getItems() 요청이 들어오면 구매건수도 추가로 전달할 수 있도록.
    //구매건수는 todo 1에서 만든 주문 테이블의 row가 몇개인지로 구하면 됨.

}
