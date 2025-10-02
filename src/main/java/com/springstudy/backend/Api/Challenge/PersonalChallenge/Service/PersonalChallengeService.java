package com.springstudy.backend.Api.Challenge.PersonalChallenge.Service;

import com.springstudy.backend.Api.Challenge.PersonalChallenge.Model.Response.PersonalChallengeDto;
import com.springstudy.backend.Api.Repository.ChallengeRepository;
import com.springstudy.backend.Api.Repository.Entity.*;
import com.springstudy.backend.Api.Repository.PersonalChallengeRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Responsev2.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonalChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PersonalChallengeRepository personalChallengeRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private UserRepository userRepository;

    //성공한 챌린지 확인용
    public List<Challenge> getSuccessChallenges(Long userId) {
        return challengeRepository.findByUserIdAndSuccessTrue(userId);
    }

    
    //개인 챌린지 저장용
    public PersonalChallenge savePersonalChallenge(
            Long userid, String personalName, String personalCompletionAction,
            int personalDuration, String personalCategory
    ) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        PersonalChallenge challenge = PersonalChallenge.builder()
                .personalChallengeName(personalName)
                .content(personalCompletionAction)
                .duration(personalDuration)
                .personalChallengeCategory(personalCategory)
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
            int personalDuration, String personalCategory
    ) {
        try {
            PersonalChallenge personalChallengeResult = savePersonalChallenge(userid, personalName, personalCompletionAction, personalDuration, personalCategory);

            return ResponseBuilder.<PersonalChallenge>create()
                    .status(HttpStatus.OK)
                    .data(personalChallengeResult)
                    .errorResponsev2(null, "개인 챌린지 저장 성공")
                    .build();


        } catch (Exception e) {
            log.error("개인챌린지 생성 실패 message: "+e.getMessage());
            return ResponseBuilder.<PersonalChallenge>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(com.springstudy.backend.Common.Responsev2.Error.DATABASE_ERROR, "개인 챌린지 저장중 에러발생")
                    .build();
        }
    }

    //UserId로 해당 user의 모든 개인챌린지 반환
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
