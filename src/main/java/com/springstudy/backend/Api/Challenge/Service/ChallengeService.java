package com.springstudy.backend.Api.Challenge.Service;

import com.springstudy.backend.Api.Challenge.Model.Response.ChallengeResponse;
import com.springstudy.backend.Api.Repository.ChallengeRepository;
import com.springstudy.backend.Api.Repository.Entity.Challenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.PrivateChallengeRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.ErrorResponsev2;
import com.springstudy.backend.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private PrivateChallengeRepository privateChallengeRepository;

    @Autowired
    private UserRepository userRepository;


    // 챌린지 완료 처리
    public boolean completeChallenge(Long userid, Long challengeId) {
        try {
            // 사용자 데이터 조회
            var user = userRepository.findById(userid).orElse(null);
            if (user == null) {
                return false;
            }

            // 챌린지 정보 조회
            Challenge challenge = challengeRepository.findById(challengeId).orElse(null);
            if (challenge == null) {
                return false;
            }

            // 사용자 정보 업데이트
            userRepository.save(user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Challenge> getSuccessChallenges(Long userId) {
        return challengeRepository.findByUseridAndSuccessTrue(userId);
    }

//    public ResponseEntity<Response<Challenge>> makeChallenge(Long userid) {
//        Response<Challenge> challenge_response = new Response<>(null, null);
//        //ChatGPTservice에 있는 챌린지를 만드는 함수에 필요한 유저 정보를 여기에 입력될 userId를 통해 검색하고 해당 값을 인자로 GPTservice를 호출해
//        //챌린지를 생성하고 해당 챌린지를 JSON 형태로 분해해서 프론트에 전달하기
//
//    }
}
