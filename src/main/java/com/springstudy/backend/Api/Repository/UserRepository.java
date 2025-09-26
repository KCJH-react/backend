package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    List<User> findAllByEmail(String email);

    @Query("""
        select distinct uc.personalChallenge from User u
        join u.userChallenges uc
        where u.id = :id""")
    List<PersonalChallenge> getPersonalChallengeByUserId(Long id);

    @Query("""
    select uc from User u
    join u.challenges uc
    where u.id = :id""")
    List<Challenge> getRandomChallengeByUserId(Long id);
}