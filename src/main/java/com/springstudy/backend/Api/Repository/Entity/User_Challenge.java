package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_challenge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User_Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_id, nullable = false")
    private PersonalChallenge personalChallenge;
}
