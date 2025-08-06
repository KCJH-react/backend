package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.PrivateChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateChallengeRepository extends JpaRepository<PrivateChallenge, Long> {
}
