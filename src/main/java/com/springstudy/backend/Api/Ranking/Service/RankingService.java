package com.springstudy.backend.Api.Ranking.Service;

import com.springstudy.backend.Api.Auth.Model.AuthUser;
import com.springstudy.backend.Api.Ranking.Model.Request.SubmitScoreRequest;
import com.springstudy.backend.Api.Ranking.Model.Response.RankingResponse;
import com.springstudy.backend.Api.Ranking.Model.Response.SubmitScoreResponse;
import com.springstudy.backend.Api.Repository.Entity.Ranking;
import com.springstudy.backend.Api.Repository.RankingRepository;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    // 점수 제출: 인증 사용자 기준으로 저장/갱신
    public SubmitScoreResponse submitScore(SubmitScoreRequest req) {
        String username = getCurrentUsername();

        Ranking r = rankingRepository
                .findByUseridAndRankType(username, req.rankType())
                .orElseGet(() -> Ranking.builder()
                        .userid(username)
                        .rankType(req.rankType())
                        .score(0)
                        .point(0L)
                        .successCount(0L)
                        .updatedAt(System.currentTimeMillis())
                        .build());

        // 비즈니스 규칙: 최고점 갱신 or 누적 포인트 업데이트 등
        // 여기서는 "점수는 최고점으로 유지, 포인트는 누적" 예시
        int newScore = req.score() != null ? req.score() : 0;
        if (r.getScore() == null || newScore > r.getScore()) {
            r.setScore(newScore);
        }
        r.setPoint((r.getPoint() == null ? 0L : r.getPoint()) + newScore);
        r.setUpdatedAt(System.currentTimeMillis());

        rankingRepository.save(r);
        return new SubmitScoreResponse(ErrorCode.SUCCESS);
    }

    // 타입별 Top10 조회
    public RankingResponse getTopRanks(String rankType) {
        List<Ranking> topList = rankingRepository.findTop10ByRankTypeOrderByScoreDesc(rankType);
        List<RankingResponse.RankingData> data = topList.stream()
                .map(r -> new RankingResponse.RankingData(r.getUserid(), r.getScore()))
                .toList();
        return new RankingResponse(ErrorCode.SUCCESS, data);
    }

    // 내 랭킹(선택): 필요 시 컨트롤러에 엔드포인트 추가해서 사용
    public RankingResponse getMyRank(String rankType) {
        String username = getCurrentUsername();
        Ranking r = rankingRepository
                .findByUseridAndRankType(username, rankType)
                .orElse(null);

        List<RankingResponse.RankingData> data = (r == null)
                ? List.of()
                : List.of(new RankingResponse.RankingData(r.getUserid(), r.getScore()));
        return new RankingResponse(ErrorCode.SUCCESS, data);
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("로그인되지 않은 사용자입니다.");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthUser authUser) {
            return authUser.getUsername();
        }
        // 다른 Principal 타입을 쓰는 경우 대비
        return authentication.getName();
    }
}
