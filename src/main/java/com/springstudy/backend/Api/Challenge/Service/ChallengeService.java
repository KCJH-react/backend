package com.springstudy.backend.Api.Challenge.Service;

import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.Challenge.Model.Response.PersonalChallengeDto;
import com.springstudy.backend.Api.Challenge.Model.Response.PersonalChallengeResponse;
import com.springstudy.backend.Api.Repository.ChallengeRepository;
import com.springstudy.backend.Api.Repository.Entity.*;
import com.springstudy.backend.Api.Repository.PersonalChallengeRepository;
import com.springstudy.backend.Api.Repository.PrivateChallengeRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.ErrorResponsev2;
import com.springstudy.backend.Common.Responsev2.Response;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PrivateChallengeRepository privateChallengeRepository;

    @Autowired
    private PersonalChallengeRepository personalChallengeRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private UserRepository userRepository;


    // 챌린지 완료 처리
    public boolean completeChallenge(Long userid, Long challengeId) {
        try {
            // 사용자 데이터 조회
            User user = userRepository.findById(userid).orElse(null);
            if (user == null) {
                return false;
            }

            // 챌린지 정보 조회
            Challenge challenge = challengeRepository.findById(challengeId).orElse(null);
            if (challenge == null) {
                return false;
            }
            int pointsToAdd = 0;
            String difficulty = challenge.getDifficulty();

            switch (difficulty) {
                case "쉬움":
                    pointsToAdd = 50;
                    break;
                case "중간":
                    pointsToAdd = 100;
                    break;
                case "어려움":
                    pointsToAdd = 150;
                    break;
                default:
                    pointsToAdd = 0;
                    break;
            }

            int currentPoints = user.getPoints();
            user.setPoints(currentPoints + pointsToAdd);

            challenge.setSuccess(true);
            challengeRepository.save(challenge);

            // 사용자 정보 업데이트
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Challenge> getSuccessChallenges(Long userId) {
        return challengeRepository.findByUserIdAndSuccessTrue(userId);
    }

    
    //개인 챌린지 저장용
    public PersonalChallenge savePersonalChallenge(
            Long userid, String personalName, String personalCompletionAction,
            Long personalDuration, String personalCategory
    ) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        PersonalChallenge challenge = PersonalChallenge.builder()
                .personalName(personalName)
                .personalCompletionAction(personalCompletionAction)
                .personalDuration(personalDuration)
                .personalCategory(personalCategory)
                .creatorId(userid)
                .build();
        personalChallengeRepository.save(challenge);

        User_Challenge userChallenge = User_Challenge.builder()
                .user(user)
                .personalChallenge(challenge)
                .build();
        userChallengeRepository.save(userChallenge);

        return challenge;
    }

    //개인 챌린지 저장 후 양식에 맞는 response 만들기
    public ResponseEntity<Response<PersonalChallenge>> savePersonalChallengeResult(
            Long userid, String personalName, String personalCompletionAction,
            Long personalDuration, String personalCategory
    ) {
        try {
            PersonalChallenge personalChallengeResult = savePersonalChallenge(userid, personalName, personalCompletionAction, personalDuration, personalCategory);

            return ResponseBuilder.<PersonalChallenge>create()
                    .status(HttpStatus.OK)
                    .data(personalChallengeResult)
                    .errorResponsev2(null, "개인 챌린지 저장 성공")
                    .build();


        } catch (Exception e) {
            return ResponseBuilder.<PersonalChallenge>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(com.springstudy.backend.Common.Responsev2.Error.DATABASE_ERROR, "개인 챌린지 저장중 에러발생")
                    .build();
        }
    }

    public List<PersonalChallengeDto> getAllPersonalChallengesByUserId(Long userId) {
        List<User_Challenge> userChallenges = userChallengeRepository.findByUserId(userId);

        return userChallenges.stream()
                .map(User_Challenge::getPersonalChallenge)
                .map(PersonalChallengeDto::new)
                .collect(Collectors.toList());
    }

    public Optional<PersonalChallenge> getPersonalChallengeById(Long challengeId) {
        return personalChallengeRepository.findById(challengeId);
    }

    //개인챌린지 완료용
    public Boolean addPointsToUser(Long userid) {
        try {
            // 사용자 데이터 조회
            User user = userRepository.findById(userid).orElse(null);
            if (user == null) {
                return false;
            }
            int pointsToAdd = 50;
            int currentPoints = user.getPoints();
            user.setPoints(currentPoints + pointsToAdd);
            // 사용자 정보 업데이트
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
