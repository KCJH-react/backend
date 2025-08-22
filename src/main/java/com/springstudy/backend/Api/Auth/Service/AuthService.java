package com.springstudy.backend.Api.Auth.Service;
import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Auth.Model.Request.UpdateRequest;
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
import com.springstudy.backend.Common.Type.Sex;
import com.springstudy.backend.Common.Type.UserInfoType;
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
                .points(0)
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

    public ResponseEntity<Response<String>> updateProfile(MultipartFile profileImg, Long id) {

        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            //예외처리
        }
        String originProfile = userOptional.get().getProfileImg();
        boolean deleteOrigin = firebaseService.deleteFile(originProfile);
        if(!deleteOrigin) {
            log.error("기존 프로필 이미지가 삭제되지 않음: {}", deleteOrigin);
        }
        try{
            firebaseService.uploadFile(profileImg.getOriginalFilename(), profileImg.getBytes(), profileImg.getContentType());
        }
        catch(IOException e){
            //예외처리
        }
        String newProfileUrl = firebaseService.getFileUrl(profileImg.getOriginalFilename());

                return ResponseBuilder.<String>create()
                .status(HttpStatus.OK)
                .data(newProfileUrl)
                .errorResponsev2(null,"프로필 이미지 업로드 및 변경 성공")
                .build();

//        try {
//            firebaseService.uploadFile(
//                    profileImg.getOriginalFilename(), profileImg.getBytes(), profileImg.getContentType());
//        } catch(IOException e){
//            return ResponseBuilder.<String>create()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .data(null)
//                    .errorResponsev2(Error.INTERNAL_SERVER_ERROR,"이미지의 입출력 처리 중 오류 발생")
//                    .build();
//        }
//        String profile_url = firebaseService.getFileUrl(profileImg.getOriginalFilename());
//
//        return ResponseBuilder.<String>create()
//                .status(HttpStatus.OK)
//                .data(profile_url)
//                .errorResponsev2(null,"프로필 이미지 업로드 성공")
//                .build();
    }

    public ResponseEntity<Response<User>> update(UpdateRequest updateRequest, Long id) {
        // 1. 비번 검사.
        // 2. 변경할 정보 유형 확인.
        // 3. 각 유형별 타입 검사.
        // 4. 데베에 실제 반영.

        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            //예외처리
            return ResponseBuilder.<User>create()
                    .data(null)
                    .errorResponsev2(Error.NOT_FOUND,"회원이 존재하지 않습니다.")
                    .status(HttpStatus.OK)
                    .build();
        }

        String user_password = userOptional.get().getUser_credential().getPassword();
        String password = updateRequest.password();
        if(!Hasher.matches(password,user_password)){
            //예외처리
            return ResponseBuilder.<User>create()
                    .data(null)
                    .errorResponsev2(Error.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        UserInfoType type = updateRequest.type();
        String content = updateRequest.content();
        if(!typeValid(type, content)){
            //예외처리
            return ResponseBuilder.<User>create()
                    .data(null)
                    .errorResponsev2(Error.BAD_REQUEST,"변경할 type의 content 형식이 일치하지 않습니다.")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        User UpdatedUser = updateUserInfo(userOptional.get(), type, content);
        UpdatedUser.setUserCredential(null);

        return ResponseBuilder.<User>create()
                .data(UpdatedUser)
                .errorResponsev2(null,"유저 정보 수정 성공")
                .status(HttpStatus.OK)
                .build();
    }

    private boolean typeValid(UserInfoType type, String content) {
        if(content == null || content.length() == 0) return false;
        switch(type){
            case USERNAME: return true;
            case PASSWORD: // 비밀번호: 최소 10자 이상, 대문자/소문자/숫자/특수문자 포함
                return content.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$");
            case EMAIL: // email repo에 이미 있는지
                return !userRepository.findByEmail(content).isPresent();
            case SEX: // 남자 | 여자 인지
                return "남자".equals(content) || "여자".equals(content);
            case BIRTHDAY: // 형식 yyyy-MM-dd 인지
                try{
                    java.time.LocalDate.parse(content, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return true;
                }catch(Exception e){return false;}
            case GOAL: return true;
        }
        return false;
    }
    private User updateUserInfo(User user, UserInfoType type, String content) {
        switch(type){
            case USERNAME: user.setUsername(content); break;
            case PASSWORD: user.getUser_credential().setPassword(Hasher.hash(content)); break;
            case EMAIL: user.setEmail(content); break;
            case SEX: user.setSex(Sex.valueOf(content)); break;
            case BIRTHDAY: user.setBirthday(content); break;
            case GOAL: user.setGoal(content); break;
        }
        return userRepository.save(user);
    }
}
