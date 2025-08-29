package com.springstudy.backend.Api.Repository.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.springstudy.backend.Common.Type.Challenge;
import com.springstudy.backend.Common.Type.Sex;
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
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private Sex sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private String birthday;

    private String goal;

    @Lob
    private String imgUrl;

    private int points;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<User_UserCategory> user_userCategoryList = new ArrayList<>();

    public void setUser_userCategory(User_UserCategory user_userCategory) {
        user_userCategoryList.add(user_userCategory);
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id", nullable = false, unique = true
    )
    private UserCredential user_credential;

    public void setUserCredential(UserCredential user_credential) {
        this.user_credential = user_credential;
    }
}
