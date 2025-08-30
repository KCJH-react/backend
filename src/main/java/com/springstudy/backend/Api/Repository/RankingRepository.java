package com.springstudy.backend.Api.Repository;

import com.springstudy.backend.Api.Repository.Entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    // rankType 별 Top10
    List<Ranking> findTop10ByRankTypeOrderByScoreDesc(String rankType);

    // 내 랭킹(특정 타입)
    Optional<Ranking> findByUseridAndRankType(String userid, String rankType);

    // 타입별 전체(친구 랭킹 등 확장 시에도 재활용)
    List<Ranking> findAllByRankTypeOrderByScoreDesc(String rankType);
}
