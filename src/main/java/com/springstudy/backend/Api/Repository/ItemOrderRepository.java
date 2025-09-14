package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.ItemOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {

    long countByItemId(Long itemId);

    // 아이템별 전체 구매건수 집계 (N+1 회피)
    @Query("select o.item.id as itemId, count(o.id) as cnt " +
            "from ItemOrder o group by o.item.id")
    List<ItemCount> countAllGroupByItem();

    List<ItemOrder> findAllByUserId(Long userId);

    interface ItemCount {
        Long getItemId();
        Long getCnt();
    }
}
