package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalChallengeRepository extends JpaRepository<PersonalChallenge, Long> {

}
