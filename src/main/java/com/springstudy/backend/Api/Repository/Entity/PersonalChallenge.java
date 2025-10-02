package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalId;

    private String personalChallengeName;
    private String content;
    private int duration;
    private String personalChallengeCategory;

    // 이 챌린지에 '참여'하는 관계 (1:N)
    @OneToMany(mappedBy = "personalChallenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Challenge> userChallenges  = new ArrayList<>();

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
}
