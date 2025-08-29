package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.PreferredChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredChallengeRepository extends JpaRepository<PreferredChallenge, Long> {
//    List<PreferredChallenge> findByuserId(Long userId);
}
