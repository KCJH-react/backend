package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.User_UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface User_UserCategoryRepository extends JpaRepository<User_UserCategory, Long> {
    User_UserCategory[] findAllByUserId(Long id);
}
