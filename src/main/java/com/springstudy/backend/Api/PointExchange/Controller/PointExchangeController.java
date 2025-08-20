package com.springstudy.backend.Api.PointExchange.Controller;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.PointExchange.Service.PointExchangeService;
import com.springstudy.backend.Api.Repository.Entity.Item;
import com.springstudy.backend.Api.Repository.ItemRepository;
import com.springstudy.backend.Common.FirebaseService;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    private final ItemRepository itemRepository;
    @GetMapping("/getItems")
    public ResponseEntity<Response<List<ItemDTO>>> getItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for(Item item : items) {
            String url = firebaseService.getFileUrl(item.getTitle());
            if(url == null) continue;
            ItemDTO itemDTO = ItemDTO.builder()
                    .id(item.getId())
                    .Url(url)
                    .points(item.getPoints())
                    .itemCategory(item.getItemCategory())
                    .points(item.getPoints())
                    .build();
            itemDTOList.add(itemDTO);
        }

        return ResponseBuilder.<List<ItemDTO>>create().status(HttpStatus.OK).errorResponsev2(null,"아이템 목록 불러오기 성공").data(itemDTOList).build();
    }

}
