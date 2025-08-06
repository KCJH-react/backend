package com.springstudy.backend.Api.Auth.Service;
import com.springstudy.backend.Api.Auth.Model.AuthUser;
import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Auth.Model.Response.LoginResponse;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.Entity.UserCredential;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.Hash.Hasher;
import com.springstudy.backend.Common.JWTToken;
import com.springstudy.backend.Common.JWTUtil;
import com.springstudy.backend.Common.RedisService;
import com.springstudy.backend.Error;
import com.springstudy.backend.ErrorResponsev2;
import com.springstudy.backend.Response;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;

    public ResponseEntity<Response<User>> signup(CreateUserRequest request) {
        // 1. 동일 이메일 있나 확인.
        // 2. 비밀번호 암호화.
        // 3. User에 추가.
        // 4. user 정보 반환.
        System.out.println("email: "+request.email()+" username: "+request.username()+"password: "+request.password()
                );
        Response<User> signup_response = new Response<>(null, null);

        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()) {
            ErrorResponsev2 errorResponsev2 = new ErrorResponsev2(Error.CONFLICT, "이미 회원가입된 정보");
            signup_response.setErrorResponsev2(errorResponsev2);
            log.error(errorResponsev2.toString());
            return new ResponseEntity(signup_response, HttpStatus.CONFLICT);
        }
        String encodedPassword = Hasher.hash(request.password());

        try{
            UserCredential userCredential = UserCredential.builder()
                    .password(encodedPassword)
                    .build();

            User newUser = User.builder()
                    .email(request.email())
                    .username(request.username())
                    .user_credential(userCredential)
                    .build();
            User savedUser = userRepository.save(newUser);

            if(savedUser == null) {
                ErrorResponsev2 errorResponsev2 = new ErrorResponsev2(Error.DATABASE_ERROR, "사용자 정보 저장 중 오류 발생");
                signup_response.setErrorResponsev2(errorResponsev2);
                log.error(errorResponsev2.toString());
                return new ResponseEntity(signup_response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("유저 생성 성공 {}",savedUser.toString());
            savedUser.setUserCredential(null);
            signup_response.setData(savedUser);
            return ResponseEntity.ok(signup_response);
        }
        catch(Exception e) {
            ErrorResponsev2 errorResponsev2 = new ErrorResponsev2(Error.DATABASE_ERROR, "사용자 정보 저장 중 오류 발생");
            signup_response.setErrorResponsev2(errorResponsev2);
            log.error(errorResponsev2.toString());
            return new ResponseEntity(signup_response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Response<User>> signin(HttpServletResponse response, LoginRequest request) {
        // 1. user 레포에 있나 확인.
        // 2. usernamePasswordAuthentication 생성
        // 3. authenticationProvier 생성 및 비밀번호 검증.
        // 4. SecurityContextHolder에 로그인 정보 저장.
        // 5. SecurityContextHolder에서 정보 가져와서 jwt 발급.
        
        Response<User> signin_response = new Response<>(null, null);

        Optional<User> user = userRepository.findByEmail(request.email());
        System.out.println("email: "+request.email()+"user: "+user.isPresent());
        if(user.isEmpty()) {
            ErrorResponsev2 errorResponsev2 = new ErrorResponsev2(Error.UNAUTHORIZED, "존재하지 않는 사용자");
            signin_response.setErrorResponsev2(errorResponsev2);
            log.error(errorResponsev2.toString());
            return new ResponseEntity(signin_response, HttpStatus.UNAUTHORIZED);
        }
        try{
            Authentication auth = authUser(user.get().getUsername(),request.password());
            JWTToken jwtToken = JWTUtil.generateToken(auth);

            User authedUser = userRepository.findByUsername(auth.getName()).get();
            log.info("login 성공 {}"+authedUser.getEmail());
            authedUser.setUserCredential(null);
            signin_response.setData(authedUser);

            ResponseCookie refreshCookie = ResponseCookie
                    .from("refresh_token")
                    .httpOnly(false)
                    .secure(false)
                    .maxAge(3600 * 2)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer "+ jwtToken.getAccessToken())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(signin_response);
        }
        catch(JwtException e) {
            //todo error
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.JWT_CREATE_ERROR);
        }
    }
    private Authentication authUser(String username, String password){
        try{
            var authentication = new UsernamePasswordAuthenticationToken(username,password);
            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(auth);
            // 인증 정보 확인
            return auth;
//            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
//            System.out.println("Authentication after setting: " + currentAuth);
        }
        catch(AuthenticationException e) {
        //todo error
        log.error(e.getMessage());
        throw new CustomException(ErrorCode.MISMATCH_PASSWORD);
    }
    }
    private Cookie createCookie(String name, String jwt){
        Cookie cookie = new Cookie(name, jwt);
        cookie.setHttpOnly(true);   // XSS 공격 방지
        cookie.setSecure(true);     // HTTPS 환경에서만 쿠키 전달 -> 배포시 true 해야 됨.
        cookie.setPath("/");        // 전체 경로에서 쿠키 사용 가능
        cookie.setMaxAge(1000000); // 1일
        cookie.setAttribute("SameSite", "None");  // 크로스 사이트 요청 허용
        return cookie;
    }
}
