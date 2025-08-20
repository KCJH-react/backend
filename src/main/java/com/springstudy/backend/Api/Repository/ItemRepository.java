package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
