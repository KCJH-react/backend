package com.springstudy.backend.Common;

import com.google.gson.internal.LinkedTreeMap;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static boolean checkURL(HttpServletRequest request, String url) {
        return request.getRequestURI().contains(url);
    }
    private String checkToken(String jwtToken, Cookie[] cookie) {

        Claims extract;
        try{
            extract = JWTUtil.extractToken(jwtToken);
            Date expiration = extract.getExpiration();

//            List<LinkedTreeMap<String, String>> rolesMap = extract.get("authorities", List.class);
//
//            List<String> roles = rolesMap.stream()
//                    .map(map -> map.get("role")) // "authority" 키를 가진 값 추출
//                    .collect(Collectors.toList());
//            if (!roles.contains("일반유저")) {
//                logger.error("권한 부족");
//                throw new CustomException(ErrorCode.JWT_ACCESS_DENIED);
//            }
            //LinkedTreeMap와stream 공부.
        }
        catch(ExpiredJwtException e){
                logger.error("JWT 토큰 만료됨");
                String refreshToken = findJWT("refreshJwt",cookie);
                //검증 2. refresh token 검증.
                Claims extractRefresh = JWTUtil.extractToken(refreshToken);
                Date expirationRefresh = extractRefresh.getExpiration();
                if (expirationRefresh.before(new Date())) {
                    // refresh토큰과 jwt 모두 만료가 지난 경우.
                    throw new CustomException(ErrorCode.JWT_EXPIRATE_PASSED);
                }
                String newJwt = JWTUtil.createTokenToRefresh(extractRefresh);
                return newJwt;
                // refresh 토큰 유효하고 jwt는 만료가 지난 경우.

        }
        catch(SignatureException e){
            logger.error(e.getMessage());
            throw new CustomException(ErrorCode.SIGNATURE_EXCEPTION);
        }
        return jwtToken;
        // jwt 토큰이 유효한 경우.
    }
    // 서명 변조, 만료일 검사, 권한검사.
    private void addContext(Claims extract){
        try{
            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("일반유저"));
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    extract.get("username").toString(), null, authorities);
            // null: jwt에는 비밀번호를 저장하지 않기 때문에.
            // db조회가 아닌 jwt로만 인증하기 때문에 세션방식과 달리
            // auth.getPrinciple()로는 username 만이 전달된다.
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        catch(NullPointerException e){
            logger.error(e.getMessage());
            throw new CustomException(ErrorCode.AUTH_SAVE_ERROR);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new CustomException(ErrorCode.FAILURE);
        }
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(checkURL(request,"/login") || checkURL(request,"/createUser")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 로그인과 회원가입에서는 필터링하지 않음.

        Cookie[] cookie = request.getCookies();
        //todo 쿠키가 없을 가능성???
        String jwtToken = findJWT("jwt", cookie);
        // 검증 1. jwt 검사.
        String extract = checkToken(jwtToken, cookie);
        Claims claims = JWTUtil.extractToken(extract);
        addContext(claims);

        filterChain.doFilter(request, response);
        System.out.println("filterclear");
    }
    private String findJWT(String name, Cookie[] cookie) {
        String jwt="";
        for(int i=0; i<cookie.length; i++){
            if(cookie[i].getName().equals(name)){
                jwt = cookie[i].getValue();
            }
        }
        return jwt;
    }
}