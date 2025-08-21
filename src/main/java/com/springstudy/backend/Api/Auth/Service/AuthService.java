package com.springstudy.backend.Api.Auth.Service;
import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Repository.Entity.PreferredChallenge;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.Entity.UserCredential;
import com.springstudy.backend.Api.Repository.Entity.User_preferedChallenge;
import com.springstudy.backend.Api.Repository.PreferredChallengeRepository;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Api.Repository.User_preferredChallengeRepository;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.FirebaseService;
import com.springstudy.backend.Common.Hash.Hasher;
import com.springstudy.backend.Common.JWTToken;
import com.springstudy.backend.Common.JWTUtil;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Common.Type.Challenge;
import com.springstudy.backend.Error;
import com.springstudy.backend.ErrorResponsev2;
import com.springstudy.backend.Response;
import io.jsonwebtoken.JwtException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PreferredChallengeRepository preferredChallengeRepository;
    private final User_preferredChallengeRepository user_preferredChallengeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final FirebaseService firebaseService;

    public ResponseEntity<Response<User>> signup(CreateUserRequest request) {
        // 1. 동일 이메일 있나 확인.
        // 2. 비밀번호 암호화.
        // 3. User에 추가.
        // 4. user 정보 반환.

        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.CONFLICT)
                    .data(null)
                    .errorResponsev2(Error.CONFLICT, "이미 회원가입된 정보")
                    .build();
        }
        String encodedPassword = Hasher.hash(request.password());

        UserCredential userCredential = UserCredential.builder()
                .password(encodedPassword)
                .build();

        User newUser = User.builder()
                .sex(request.sex())
                .birthday(request.birthday())
                .goal(request.goal())
                .email(request.email())
                .username(request.username())
                .profileImg(request.imgUrl())
                .user_credential(userCredential)
                .build();

        try{
            User savedUser = userRepository.save(newUser);

            for(Challenge c : request.preferredChallenge()){
                PreferredChallenge pc = PreferredChallenge.builder().challenge(c).build();
                preferredChallengeRepository.save(pc);
                User_preferedChallenge u_p = User_preferedChallenge.builder().user(savedUser).preferredChallenge(pc).build();
                user_preferredChallengeRepository.save(u_p);
            }
            savedUser.setUserCredential(null);
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.OK)
                    .data(savedUser)
                    .errorResponsev2(null, "유저 생성 성공 " + savedUser.toString())
                    .build();
        }
        catch(Exception e) {
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(Error.DATABASE_ERROR, "사용자 정보 저장 중 오류 발생")
                    .build();
        }
    }

    public ResponseEntity<Response<User>> signin(LoginRequest request) {
        // 1. user 레포에 있나 확인.
        // 2. usernamePasswordAuthentication 생성
        // 3. authenticationProvier 생성 및 비밀번호 검증.
        // 4. SecurityContextHolder에 로그인 정보 저장.
        // 5. SecurityContextHolder에서 정보 가져와서 jwt 발급.
        
        Response<User> signin_response = new Response<>(null, null);

        Optional<User> user = userRepository.findByEmail(request.email());
        System.out.println("email: "+request.email()+"user: "+user.isPresent());
        if(user.isEmpty()) {
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.UNAUTHORIZED)
                    .data(null)
                    .errorResponsev2(Error.UNAUTHORIZED, "존재하지 않는 사용자")
                    .build();
        }
        try{
            Authentication auth = authUser(user.get().getUsername(),request.password());
            User authedUser = userRepository.findByUsername(auth.getName()).get();
            JWTToken jwtToken = JWTUtil.generateToken(auth);
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
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(null)
                    .errorResponsev2(Error.INTERNAL_SERVER_ERROR, "서버 내 JWT 생성 오류")
                    .build();
        }
        catch(AuthenticationException e) {
            return ResponseBuilder.<User>create()
                    .status(HttpStatus.UNAUTHORIZED)
                    .data(null)
                    .errorResponsev2(Error.UNAUTHORIZED, "존재하지 않는 사용자")
                    .build();
        }
    }
    private Authentication authUser(String username, String password) throws AuthenticationException {
            var authentication = new UsernamePasswordAuthenticationToken(username,password);
            Authentication auth = authenticationManagerBuilder.getObject().authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(auth);
            // 인증 정보 확인
            return auth;
    }

    public ResponseEntity<Response<String>> uploadProfile(MultipartFile profileImg) {

            try {
                firebaseService.uploadFile(
                        profileImg.getOriginalFilename(), profileImg.getBytes(), profileImg.getContentType());
            } catch(IOException e){
                return ResponseBuilder.<String>create()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(null)
                        .errorResponsev2(Error.INTERNAL_SERVER_ERROR,"이미지의 입출력 처리 중 오류 발생")
                        .build();
            }
            String profile_url = firebaseService.getFileUrl(profileImg.getOriginalFilename());

            return ResponseBuilder.<String>create()
                    .status(HttpStatus.OK)
                    .data(profile_url)
                    .errorResponsev2(null,"프로필 이미지 업로드 성공")
                    .build();
    }
}
