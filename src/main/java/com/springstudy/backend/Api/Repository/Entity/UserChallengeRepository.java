package com.springstudy.backend.Api.Repository.Entity;

import com.springstudy.backend.Api.Repository.Entity.User_Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserChallengeRepository extends JpaRepository<User_Challenge, Long> {

    List<User_Challenge> findByUserId(Long userId);
}