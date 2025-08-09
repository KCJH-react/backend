package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
<<<<<<<< HEAD:src/main/java/com/springstudy/backend/Api/Repository/Entity/UserCredential.java
@Table(name = "user_credentials")
public class UserCredential {
========
@Table(name = "userCredentional")
public class UserCredentional {
>>>>>>>> jhs2:src/main/java/com/springstudy/backend/Api/Repository/Entity/UserCredentional.java

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;
}
