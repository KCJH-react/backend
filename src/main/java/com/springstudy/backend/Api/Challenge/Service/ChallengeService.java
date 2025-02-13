package com.springstudy.backend.Api.Challenge.Service;

import com.springstudy.backend.Api.Repoitory.ChallengeRepository;
import com.springstudy.backend.Api.Repoitory.Entity.Challenge;
import com.springstudy.backend.Api.Repoitory.PrivateChallengeRepository;
import com.springstudy.backend.Api.Repoitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {

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
            Challenge challenge = ChallengeRepository.findById(challengeId).orElse(null);
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
}
