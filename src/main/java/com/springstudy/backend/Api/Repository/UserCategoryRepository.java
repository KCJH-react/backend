package com.springstudy.backend.Api.Repository;
import com.springstudy.backend.Api.Repository.Entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
