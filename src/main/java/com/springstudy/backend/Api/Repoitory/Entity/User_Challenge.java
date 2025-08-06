package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_challenge")
@Getter
@Setter
@NoArgsConstructor
public class User_Challenge {

    @EmbeddedId // 위에서 만든 복합 키 클래스를 사용
    private User_ChallengeId id;

    // 복합 키의 userId 필드를 User 엔티티와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // ChallengeParticipationId의 'userId' 필드에 매핑
    @JoinColumn(name = "user_id")
    private User user;

    // 복합 키의 challengeId 필드를 PersonalChallenge 엔티티와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("challengeId") // ChallengeParticipationId의 'challengeId' 필드에 매핑
    @JoinColumn(name = "challenge_id")
    private PersonalChallenge challenge;
}
