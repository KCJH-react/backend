package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal_challenges")
@Getter
@Setter
@NoArgsConstructor
public class PersonalChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personal_id")
    private Long id;

    // '생성자' User 와의 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false) // FK 컬럼 이름 지정
    private User creator;

    @Column(nullable = false, length = 100)
    private String personalName;
    private String personalCompletionAction;
    private Long personalDuration;
    private String personalIcon;

    // 이 챌린지에 '참여'하는 관계 (1:N)
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Challenge> participants = new ArrayList<>();
}
