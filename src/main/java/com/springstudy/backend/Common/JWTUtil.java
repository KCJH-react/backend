package com.springstudy.backend.Common;

import com.springstudy.backend.Api.Auth.Model.AuthUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
// JWT 만들어주는 함수
public class JWTUtil {
    static final SecretKey key =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(
                    "jwtpassword123jwtpassword123jwtpassword123jwtpassword123jwtpassword"
            ));
    public static String createToken(Authentication auth) {
        // auth: JWT로 회원정보를 저장해야 되기 때문에.
        AuthUser user = (AuthUser) auth.getPrincipal();

        String jwt = Jwts.builder()
                .claim("displayName", user.getUsername())
                // .claim CustomUser 정보를 저장하는 메소드.
                // .claim: 저장할 정보 추가.
                .issuedAt(new Date(System.currentTimeMillis()))
                // .issuedAt: 생성날짜를 생성하는 메소드.
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //유효기간 1시간
                // .expiration: 만료기간을 설정하는 메소드.
                .signWith(key)
                .compact();
        return jwt;
    }
}
