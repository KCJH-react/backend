package com.springstudy.backend.Api.PointExchange.Service;

import com.springstudy.backend.Api.PointExchange.Model.Request.PointExchangeRequest;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.ItemOrderDTO;
import com.springstudy.backend.Api.PointExchange.Model.Response.PointExchangeResponse;
import com.springstudy.backend.Api.Repository.Entity.Item;
import com.springstudy.backend.Api.Repository.Entity.ItemOrder;
import com.springstudy.backend.Api.Repository.Entity.PointExchange;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.ItemOrderRepository;
import com.springstudy.backend.Api.Repository.ItemRepository;
import com.springstudy.backend.Api.Repository.PointExchangeRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.FirebaseService;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Error;
import com.springstudy.backend.Common.Responsev2.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointExchangeService {

    private final PointExchangeRepository pointExchangeRepository;
    private final ItemRepository itemRepository;
    private final FirebaseService firebaseService;

    // ✅ 추가: 주문/유저 리포지토리
    private final ItemOrderRepository itemOrderRepository;
    private final UserRepository userRepository;

    // 포인트 사용
    public PointExchangeResponse usePoints(PointExchangeRequest request) {
        PointExchange exchange = PointExchange.builder()
                .username(request.getUsername())
                .points(request.getPoints())
                .exchangeType("USE")
                .build();
        PointExchange saved = pointExchangeRepository.save(exchange);
        return convertToResponse(saved);
    }

    // 포인트 적립
    public PointExchangeResponse earnPoints(PointExchangeRequest request) {
        PointExchange exchange = PointExchange.builder()
                .username(request.getUsername())
                .points(request.getPoints())
                .exchangeType("EARN")
                .build();
        PointExchange saved = pointExchangeRepository.save(exchange);
        return convertToResponse(saved);
    }

    // 거래 내역
    public List<PointExchangeResponse> getUserPointHistory(String username) {
        return pointExchangeRepository.findByUsername(username)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PointExchangeResponse convertToResponse(PointExchange e) {
        return PointExchangeResponse.of(
                e.getUsername(),
                e.getPoints(),
                e.getExchangeType(),
                e.getExchangeDate()
        );
    }


    // ✅ 아이템 목록 + (전체) 구매건수 포함
    public ResponseEntity<Response<List<ItemDTO>>> getItems() {
        List<Item> items = itemRepository.findAll();

        // 아이템별 전체 구매건수 집계
        Map<Long, Long> countMap = itemOrderRepository.countAllGroupByItem()
                .stream()
                .collect(Collectors.toMap(ItemOrderRepository.ItemCount::getItemId,
                        ItemOrderRepository.ItemCount::getCnt));

        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (Item item : items) {
            long purchaseCount = countMap.getOrDefault(item.getId(), 0L);
            ItemDTO itemDTO = ItemDTO.builder()
                    .id(item.getId())
                    .Url(item.getUrl())
                    .title(item.getTitle())
                    .itemCategory(item.getItemCategory())
                    .points(item.getPoints())
                    .purchaseCount(purchaseCount) // ✅ 추가
                    .build();
            itemDTOList.add(itemDTO);
        }

        return ResponseBuilder.<List<ItemDTO>>create()
                .status(HttpStatus.OK)
                .errorResponsev2(null, "아이템 목록 불러오기 성공")
                .data(itemDTOList)
                .build();
    }

    // ✅ 그대로 유지: 파이어베이스 이미지 URL 프리로드
    public void preloadImg() {
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            if (item.getUrl() != null) continue; // 나중에 지울 것.
            String url = firebaseService.getFileUrl(item.getImgTitle());
            item.setUrl(url);
            itemRepository.save(item);
        }
        System.out.println("✅ 파이어베이스 이미지 URL 프리로드 완료");
    }

    // ✅ 주문 생성(todo1): username → userId 매핑 후 주문 저장
    @Transactional
    public ResponseEntity<Response<String>> createOrder(Long id, Long itemId, int quantity) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자: " + id));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이템: " + itemId));

        int sumPoints = item.getPoints() + quantity;
        if(user.getPoints() < sumPoints){
            //예외처리
            return ResponseBuilder.<String>create()
                    .data(null)
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .errorResponsev2(Error.VALIDATION_ERROR, "포인트가 부족합니다")
                    .build();
        }

        int balance = user.getPoints() - sumPoints;
        user.setPoints(balance);

        ItemOrder order = ItemOrder.builder()
                .user(user)
                .item(item)
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .useCode(UUID.randomUUID().toString().replace("-", "").substring(0,16).toUpperCase())
                .build();

        userRepository.save(user);
        itemOrderRepository.save(order);

        return ResponseBuilder.<String>create()
                .status(HttpStatus.OK)
                .errorResponsev2(Error.OK, "주문 처리 완료")
                .data(String.valueOf(balance))
                .build();
    }

    public ResponseEntity<Response<List<ItemOrderDTO>>> getMyItem(Long userId){
        // 1. 아이디 확인
        // 2. 아이템 불러오기
        // 3. 데이터 가공 및 반환.

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){
            //예외처리
        }
        List<ItemOrder> itemOrders = itemOrderRepository.findAllByUserId(userId);

        if(itemOrders.size() == 0){
            // 예외처리
        }
        List<ItemOrderDTO> itemOrderDTOList = new ArrayList<>();
        for(ItemOrder itemOrder : itemOrders){
            String itemTitle = itemOrder.getItem().getTitle();
            ItemOrderDTO itemOrderDTO = ItemOrderDTO.builder()
                    .useCode(itemOrder.getUseCode())
                    .createdAt(itemOrder.getCreatedAt())
                    .id(itemOrder.getId())
                    .itemTitle(itemTitle)
                    .build();
            itemOrderDTOList.add(itemOrderDTO);
        }

        return ResponseBuilder.<List<ItemOrderDTO>>create()
                .data(itemOrderDTOList)
                .errorResponsev2(Error.OK, "내 아이템 목록 반환 성공")
                .status(HttpStatus.OK)
                .build();
    }
}
