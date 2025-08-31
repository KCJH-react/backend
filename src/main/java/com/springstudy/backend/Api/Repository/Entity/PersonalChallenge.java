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

    private String personalName;
    private String personalCompletionAction;
    private Long personalDuration;
    private String personalIcon;
    private String personalCategory;

    // 이 챌린지에 '참여'하는 관계 (1:N)
    @OneToMany(mappedBy = "personalChallenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Challenge> userChallenges  = new ArrayList<>();

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

}
