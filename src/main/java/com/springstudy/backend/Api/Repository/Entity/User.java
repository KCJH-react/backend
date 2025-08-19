package com.springstudy.backend.Api.Repository.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springstudy.backend.Common.Type.Challenge;
import com.springstudy.backend.Common.Type.Sex;
import jakarta.persistence.*;
import lombok.*;

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

    //private String profileImg; s3로 추후 구현
    
    private Sex sex;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private String birthday;

    private String goal;

    private int points;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credential_id", referencedColumnName = "id", nullable = false, unique = true,
    foreignKey = @ForeignKey(name = "fk_users_user_credentials"))
    private UserCredential user_credential;

    public void setUserCredential(UserCredential user_credential) {
        this.user_credential = user_credential;
    }
}
