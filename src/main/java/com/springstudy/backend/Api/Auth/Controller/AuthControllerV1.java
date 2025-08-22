package com.springstudy.backend.Api.Auth.Controller;

import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Auth.Model.Request.UpdateRequest;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class AuthControllerV1 {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Response<User>> signup(@RequestBody CreateUserRequest createUserRequest) {
        return authService.signup(createUserRequest);
    }
    @PostMapping("/signin")
    public ResponseEntity<Response<User>> signin(@RequestBody LoginRequest loginRequest,
                                                 HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getCookies());
        return authService.signin(loginRequest);
    }
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<String>> profileUpload(@RequestPart MultipartFile profileImg){
        return authService.uploadProfile(profileImg);
    }

    @PutMapping(value = "/profile/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<String>> profileUpdate(@RequestPart MultipartFile profileImg, @PathVariable Long id){
        return authService.updateProfile(profileImg, id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Response<User>>  update(UpdateRequest updateRequest, @PathVariable Long id) {
        return authService.update(updateRequest, id);
    }
}
