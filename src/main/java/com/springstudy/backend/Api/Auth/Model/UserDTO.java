package com.springstudy.backend.Api.Auth.Model;

import com.springstudy.backend.Common.Type.Challenge;
import com.springstudy.backend.Common.Type.Sex;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @Lob
    private String imgUrl;
    private String username;
    private String email;
    private Sex sex;
    private String birthday;
    private int points;
    private String goal;
    private List<Challenge> category;
}

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String username;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    private Sex sex;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
//    private String birthday;
//
//    private String goal;
//
//    @Lob
//    private String imgUrl;
//
//    private int points;
//
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_credential_id", referencedColumnName = "id", nullable = false, unique = true
//    )
//    private UserCredential user_credential;
//
//    public void setUserCredential(UserCredential user_credential) {
//        this.user_credential = user_credential;
//    }