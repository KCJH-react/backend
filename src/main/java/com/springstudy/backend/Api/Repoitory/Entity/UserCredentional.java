package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "userCredentional")
public class UserCredentional {

    @Id
    @OneToOne
    @JoinColumn(name = "userid")
    private User user;

    @Column(nullable = false)
    private String password;
}
