package com.springstudy.backend.Common;

import com.google.gson.internal.LinkedTreeMap;
import com.springstudy.backend.Api.Auth.Model.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
// JWT 만들어주는 함수
@RequiredArgsConstructor
public class JWTUtil {
    private static final SecretKey key =
            Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static JWTToken generateToken(Authentication auth, Long userId){
        // 1. 인증된 auth 정보를 받아 해싱함.
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .setSubject(userId.toString())
                .claim("authorities", authorities)
                .expiration(new Date(now + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        String refreshToken = Jwts.builder()
                .setSubject(auth.getName())
                .setId(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(now + 2 * 60 * 60 * 1000))
                .compact();

        return JWTToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(authorities)
                .build();
    }

    public static boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(MalformedJwtException | SecurityException e){

        }
        catch(ExpiredJwtException e){

        }
        catch(UnsupportedJwtException e){

        }
        catch(IllegalArgumentException e){

        }
        return false;
    }

    public static Long getUserId(String token){
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Long userId =Long.parseLong(claims.getSubject());
        return userId;
    }

//    public static String createToken(Authentication auth) {
//        // auth: JWT로 회원정보를 저장해야 되기 때문에.
//        AuthUser user = (AuthUser) auth.getPrincipal();
//
//        String jwt = Jwts.builder()
//                .claim("username", user.getUsername())
//                // .claim CustomUser 정보를 저장하는 메소드.
//                // .claim: 저장할 정보 추가.
//                .claim("authorities", user.getAuthorities())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                // .issuedAt: 생성날짜를 생성하는 메소드.
//                .expiration(new Date(System.currentTimeMillis() + 10)) //유효기간 1시간
//                // .expiration: 만료기간을 설정하는 메소드.
//                .signWith(key)
//                .compact();
//        return jwt;
//    }
//
//    public static String createTokenToRefresh(Claims extract) {
//        // auth: JWT로 회원정보를 저장해야 되기 때문에.
//        String username =extract.get("username", String.class);
//        List<LinkedTreeMap<String, String>> rolesMap = extract.get("authorities", List.class);
//        List<String> roles = rolesMap.stream()
//                .map(map -> map.get("role")) // "authority" 키를 가진 값 추출
//                .collect(Collectors.toList());
//
//        String jwt = Jwts.builder()
//                .claim("username", username)
//                // .claim CustomUser 정보를 저장하는 메소드.
//                // .claim: 저장할 정보 추가.
//                .claim("authorities", rolesMap)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                // .issuedAt: 생성날짜를 생성하는 메소드.
//                .expiration(new Date(System.currentTimeMillis() + 360000)) //유효기간 1시간
//                // .expiration: 만료기간을 설정하는 메소드.
//                .signWith(key)
//                .compact();
//        return jwt;
//    }
//
//    // JWT 까주는 함수
//    public static Claims extractToken(String token) {
//        Claims claims = Jwts.parser().verifyWith(key).build()
//                .parseSignedClaims(token).getPayload();
//        return claims;
//    }
//    public static String createRefreshToken(Authentication auth) {
//        // auth: JWT로 회원정보를 저장해야 되기 때문에.
//        AuthUser user = (AuthUser) auth.getPrincipal();
//
//        String refreshJwt = Jwts.builder()
//                .claim("username", user.getUsername())
//                // .claim CustomUser 정보를 저장하는 메소드.
//                // .claim: 저장할 정보 추가.
//                .claim("authorities", user.getAuthorities())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                // .issuedAt: 생성날짜를 생성하는 메소드.
//                .expiration(new Date(System.currentTimeMillis() + 1000000)) //유효기간 1시간
//                // .expiration: 만료기간을 설정하는 메소드.
//                .signWith(key)
//                .compact();
//        return refreshJwt;
//    }
}
