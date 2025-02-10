package com.springstudy.backend.Api.PrivateChallenge.Service;


import com.springstudy.backend.Api.Repoitory.Entity.PrivateChallenge;
import com.springstudy.backend.Api.PrivateChallenge.Model.Request.PrivateChallengeRequest;
import com.springstudy.backend.Api.PrivateChallenge.Model.Response.PrivateChallengeResponse;
import com.springstudy.backend.Api.Repoitory.PrivateChallengeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j //
public class PrivateChallengeService {

    private final PrivateChallengeRepository privateChallengeRepository;

    public PrivateChallengeResponse createPrivateChallenge(PrivateChallengeRequest request) {
        //새로운 챌린지 생성
        PrivateChallenge newChallenge = PrivateChallenge.builder()
                .category(request.getCategory())
                .difficulty(request.getDifficulty())
                .name(request.getName())
                .points(request.getPoints())
                .build();
        //데이터베이스에 저장
        PrivateChallenge savedChallenge = privateChallengeRepository.save(newChallenge);

        return new PrivateChallengeResponse(
                savedChallenge.getId(),
                savedChallenge.getCategory(),
                savedChallenge.getDifficulty(),
                savedChallenge.getName(),
                savedChallenge.getPoints()
        );
    }
}
