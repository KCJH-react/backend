package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "rankings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_ranking_user_type", columnNames = {"userid", "rank_type"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인증된 사용자명 (AuthUser.getUsername())
    @Column(nullable = false, length = 100)
    private String userid;

    // ERD 기준: 랭크 구분 (예: TOTAL, WEEKLY, FRIEND 등)
    @Column(name = "rank_type", nullable = false, length = 30)
    private String rankType;

    // 선택: 랭킹 순위(배치 작업으로 계산하거나, 조회 시 계산할 수 있음)
    @Column(name = "rank_position")
    private Integer rankPosition;

    // 누적 포인트/점수 분리 운용 시 사용 (선택)
    @Column(name = "points")
    private Long point;

    // 도전 성공 횟수(선택)
    @Column(name = "success_count")
    private Long successCount;

    // 갱신 시각 (epoch millis). 프로젝트에서 LocalDateTime 이슈가 있었으므로 Long 유지.
    @Column(name = "updated_at")
    private Long updatedAt;

    // 기본 점수(이번 제출 등)
    @Column(name = "score")
    private Integer score;

    public Ranking(String userid, String rankType, int score) {
        this.userid = userid;
        this.rankType = rankType;
        this.score = score;
    }
}
