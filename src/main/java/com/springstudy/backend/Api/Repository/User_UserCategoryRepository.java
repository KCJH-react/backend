package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.User_UserCategory;
import com.springstudy.backend.Common.Type.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface User_UserCategoryRepository extends JpaRepository<User_UserCategory, Long> {
    User_UserCategory[] findAllByUserId(Long id);
    @Query("SELECT uuc.userCategory.challenge " +
            "FROM User_UserCategory uuc " +
            "WHERE uuc.user.id = :userId")
    List<Challenge> findChallengesByUserId(@Param("userId") Long userId);
}
