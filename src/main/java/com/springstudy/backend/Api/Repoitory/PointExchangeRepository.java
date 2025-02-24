package com.springstudy.backend.Api.Repoitory;

import com.springstudy.backend.Api.Repoitory.Entity.PointExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointExchangeRepository extends JpaRepository<PointExchange, Long> {
    List<PointExchange> findByUsername(String username);
}
