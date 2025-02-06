package com.springstudy.backend.Api.Auth.Service;

import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Response.CreateUserResponse;
import com.springstudy.backend.Api.Repoitory.Entity.User;
import com.springstudy.backend.Api.Repoitory.Entity.UserCredentional;
import com.springstudy.backend.Api.Repoitory.UserRepository;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.Hash.Hasher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateUserService {
    private final UserRepository userRepository;

    public CreateUserResponse createUser(CreateUserRequest request) {
        // 1. 동일 이메일 있나 확인.
        // 2. 비밀번호 암호화.
        // 3. User에 추가.
        // 4. 성공 코드 반환.

        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()) {
            return new CreateUserResponse(ErrorCode.USER_ALREADY_EXISTS);
        }
        String encodedPassword = Hasher.hash(request.password());

        try{
            User newUser = User.builder()
                    .email(request.email())
                    .username(request.username())
                    .user_id(request.user_id())
                    .build();
            UserCredentional userCredentional = UserCredentional.builder()
                    .user(newUser)
                    .password(encodedPassword)
                    .build();
            newUser.setUserCredentional(userCredentional);
            User savedUser = userRepository.save(newUser);

            if(savedUser == null) {
                return new CreateUserResponse(ErrorCode.USER_CREATE_FAILED);
            }
        }
        catch(Exception e) {
            return new CreateUserResponse(ErrorCode.USER_CREATE_FAILED);
        }

        return new CreateUserResponse(ErrorCode.SUCCESS);
    }
}
