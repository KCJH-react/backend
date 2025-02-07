package com.springstudy.backend.Api.Auth.Service;
import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Auth.Model.Response.CreateUserResponse;
import com.springstudy.backend.Api.Auth.Model.Response.LoginResponse;
import com.springstudy.backend.Api.Repoitory.Entity.User;
import com.springstudy.backend.Api.Repoitory.Entity.UserCredentional;
import com.springstudy.backend.Api.Repoitory.UserRepository;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.Hash.Hasher;
import com.springstudy.backend.Common.JWTUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j //
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public CreateUserResponse createUser(CreateUserRequest request) {
        // 1. 동일 이메일 있나 확인.
        // 2. 비밀번호 암호화.
        // 3. User에 추가.
        // 4. 성공 코드 반환.

        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()) {
            log.error("USER_ALREADY_EXISTS: {}");
            return new CreateUserResponse(ErrorCode.USER_ALREADY_EXISTS);
        }
        String encodedPassword = Hasher.hash(request.password());

        try{
            User newUser = User.builder()
                    .email(request.email())
                    .username(request.username())
                    .userid(request.userid())
                    .build();
            UserCredentional userCredentional = UserCredentional.builder()
                    .user(newUser)
                    .password(encodedPassword)
                    .build();
            newUser.setUserCredentional(userCredentional);
            User savedUser = userRepository.save(newUser);

            if(savedUser == null) {
                log.error("USER_CREATE_FAILED: {}+ null 값 저장 오류");
                return new CreateUserResponse(ErrorCode.USER_CREATE_FAILED);
            }
        }
        catch(Exception e) {
            log.error("USER_CREATE_FAILED: {}");
            return new CreateUserResponse(ErrorCode.USER_CREATE_FAILED);
        }

        return new CreateUserResponse(ErrorCode.SUCCESS);
    }

    public LoginResponse login(HttpServletResponse response, LoginRequest request) {
        // 1. user 레포에 있나 확인.
        // 2. usernamePasswordAuthentication 생성
        // 3. authenticationProvier 생성 및 비밀번호 검증.
        // 4. SecurityContextHolder에 로그인 정보 저장.
        // 5. SecurityContextHolder에서 정보 가져와서 jwt 발급.

        Optional<User> user = userRepository.findByUserid(request.userid());
        if(user.isEmpty()) {
            //todo error
        }

        try{
            authUser(user.get().getUsername(),request.password());
        }
        catch(AuthenticationException e) {
            //todo error
        }

        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null){
                //todo error
            }
            String jwt = JWTUtil.createToken(auth);
            Cookie cookie= createCookie(jwt);
            response.addCookie(cookie);
        }
        catch(JwtException e) {
            //todo error
        }

        return new LoginResponse(ErrorCode.SUCCESS);
    }
    private void authUser(String username, String password) throws AuthenticationException {
        var authentication = new UsernamePasswordAuthenticationToken(username,password);
        Authentication auth = authenticationManagerBuilder.getObject().authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    private Cookie createCookie(String jwt){
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setMaxAge(10);
        // 만료기간 10초로 설정한다.
        cookie.setHttpOnly(true);
        // xss 공격을 방지한다.
        cookie.setPath("/");
        // 모든 경로에서 쿠키를 전달한다.
        return cookie;
    }
}
