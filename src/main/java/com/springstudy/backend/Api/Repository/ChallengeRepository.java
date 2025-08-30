package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    List<Challenge> findByUseridAndSuccessTrue(Long userId);
}
