package com.springstudy.backend.Api.Ranking.Service;

import com.springstudy.backend.Api.Ranking.Model.Request.SubmitScoreRequest;
import com.springstudy.backend.Api.Ranking.Model.Response.RankingResponse;
import com.springstudy.backend.Api.Ranking.Model.Response.SubmitScoreResponse;
import com.springstudy.backend.Api.Repoitory.Entity.Ranking;
import com.springstudy.backend.Api.Repoitory.RankingRepository;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Api.Auth.Model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingRepository rankingRepository;

    public SubmitScoreResponse submitScore(SubmitScoreRequest request) {
        try {
            String userid = getCurrentUserId();
            if (userid == null || userid.isBlank()) {
                return new SubmitScoreResponse(ErrorCode.FAILURE);
            }

            // ✅ 기존 데이터가 있으면 업데이트, 없으면 생성
            Ranking ranking = rankingRepository.findByUserid(userid)
                    .orElse(new Ranking(userid, 0));

            ranking.setScore(ranking.getScore() + request.score()); // ✅ 점수 누적
            rankingRepository.save(ranking);

            return new SubmitScoreResponse(ErrorCode.SUCCESS);
        } catch (Exception e) {
            return new SubmitScoreResponse(ErrorCode.FAILURE);
        }
    }

    public RankingResponse getTopRanks() {
        List<RankingResponse.RankingData> rankings =
                Optional.ofNullable(rankingRepository.findTop10ByOrderByScoreDesc())
                        .orElse(List.of())
                        .stream()
                        .map(ranking -> new RankingResponse.RankingData(ranking.getUserid(), ranking.getScore()))
                        .collect(Collectors.toList());

        return new RankingResponse(ErrorCode.SUCCESS, rankings);
    }

    private String getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인되지 않은 사용자입니다.");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthUser authUser) {
            //return authUser.getUserId();
            return "id";
        } else {
            throw new RuntimeException("유효하지 않은 사용자 정보입니다.");
        }
    }
}
