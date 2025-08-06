package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.PointExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointExchangeRepository extends JpaRepository<PointExchange, Long> {
    List<PointExchange> findByUsername(String username);
}
