package com.springstudy.backend.Api.Repoitory;

import com.springstudy.backend.Api.Repoitory.Entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findTop10ByOrderByScoreDesc();
    Optional<Ranking> findByUserid(String userid);
}
