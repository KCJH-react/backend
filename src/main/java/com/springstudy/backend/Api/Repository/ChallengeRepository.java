package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    List<Challenge> findByUseridAndSuccessTrue(Long userId);

    @Query("SELECT c FROM Challenge c " +
            "WHERE c.userid = :userId " +
            "AND c.createdAt BETWEEN :startOfDay AND :endOfDay " +
            "ORDER BY c.createdAt DESC")
    List<Challenge> findTodaysChallengesByUserId(@Param("userId") Long userId,
                                                 @Param("startOfDay") LocalDateTime startOfDay,
                                                 @Param("endOfDay") LocalDateTime endOfDay);

}
