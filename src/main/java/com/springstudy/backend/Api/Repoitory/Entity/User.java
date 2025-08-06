package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String img;
    private Long points;
    private String goal;
    private String category;
    private String sex;
    private LocalDate birthday;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonalChallenge> createdChallenges = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User_Challenge> participations = new ArrayList<>();

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserCredentional user_credentional;

    public void setUserCredentional(UserCredentional user_credentional) {
        this.user_credentional = user_credentional;
    }
}
