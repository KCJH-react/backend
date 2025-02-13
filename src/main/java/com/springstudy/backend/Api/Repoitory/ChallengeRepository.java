package com.springstudy.backend.Api.Repoitory;

import com.springstudy.backend.Api.Repoitory.Entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}