package com.springstudy.backend.Api.PointExchange.Service;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.Repository.Entity.Item;
import com.springstudy.backend.Api.Repository.Entity.PointExchange;
import com.springstudy.backend.Api.Repository.ItemRepository;
import com.springstudy.backend.Api.Repository.PointExchangeRepository;
import com.springstudy.backend.Common.FirebaseService;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Response;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointExchangeService {

    private final PointExchangeRepository pointExchangeRepository;
    private final ItemRepository itemRepository;
    private final FirebaseService firebaseService;

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

    public ResponseEntity<Response<List<ItemDTO>>> getItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for(Item item : items) {
            ItemDTO itemDTO = ItemDTO.builder()
                    .Url(item.getUrl())
                    .title(item.getTitle())
                    .itemCategory(item.getItemCategory())
                    .points(item.getPoints())
                    .build();
            itemDTOList.add(itemDTO);
        }

        return ResponseBuilder.<List<ItemDTO>>create()
                .status(HttpStatus.OK)
                .errorResponsev2(null,"아이템 목록 불러오기 성공")
                .data(itemDTOList)
                .build();

    }

    public void preloadImg(){
        List<Item> items = itemRepository.findAll();
        for(Item item : items) {
            if(item.getUrl() != null) continue; // 나중에 지울 것.
            String url = firebaseService.getFileUrl(item.getImgTitle());
            item.setUrl(url);
            itemRepository.save(item);
        }
        System.out.println("✅ 파이어베이스 이미지 URL 프리로드 완료");
    }
}
