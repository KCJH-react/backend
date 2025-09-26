package com.springstudy.backend.Api.Auth.Service;

import com.springstudy.backend.Api.Auth.Model.DTOS.PersonalChallengeDTO;
import com.springstudy.backend.Api.Auth.Model.DTOS.RandomChallengeDTO;
import com.springstudy.backend.Api.Challenge.Model.Response.PersonalChallengeDto;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.PersonalChallenge;
import com.springstudy.backend.Api.Repository.Entity.PrivateChallenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.PersonalChallengeRepository;
import com.springstudy.backend.Api.Repository.PrivateChallengeRepository;
import com.springstudy.backend.Api.Repository.UserCategoryRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.FirebaseService;
import com.springstudy.backend.Common.RedisService;
import com.springstudy.backend.Common.Responsev3.ResponseV3;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthInfoService {
    private final UserRepository userRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final FirebaseService firebaseService;
    private final PersonalChallengeRepository personalChallengeRepository;

    public ResponseEntity<ResponseV3<List<PersonalChallengeDTO>>> getPersonalChallenge(Long userid) {
        // 회원 체크 - 개인 챌린지 불러오기
        Optional<User> userOptional = userRepository.findById(userid);
        if (userOptional.isEmpty()) {
            //예외처리
            throw new RuntimeException("user not found");
        }
        List<PersonalChallenge> privateChallenges = userRepository.getPersonalChallengeByUserId(userid);
        List<PersonalChallengeDTO> personalChallengeDTOS = new ArrayList<>();
        for(PersonalChallenge pc : privateChallenges) {
            personalChallengeDTOS.add(new PersonalChallengeDTO(pc));
        }
        return ResponseEntity.ok(new ResponseV3(personalChallengeDTOS, LocalDateTime.now(), "개인 챌린지 불러오기 성공"));
    }

    public ResponseEntity<ResponseV3<List<RandomChallengeDTO>>> getRandomChallenge(Long userid) {
        // 회원 체크 - 개인 챌린지 불러오기
        Optional<User> userOptional = userRepository.findById(userid);
        if (userOptional.isEmpty()) {
            //예외처리
            throw new RuntimeException("user not found");
        }
        List<Challenge> randomChallenges = userRepository.getRandomChallengeByUserId(userid);
        List<RandomChallengeDTO> randomChallengeDTOs = new ArrayList<>();
        for(Challenge rc : randomChallenges) {
            randomChallengeDTOs.add(new RandomChallengeDTO(rc));
        }
        return ResponseEntity.ok(new ResponseV3(randomChallengeDTOs, LocalDateTime.now(), "개인 챌린지 불러오기 성공"));
    }
}