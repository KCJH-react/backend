package com.springstudy.backend.Api.Auth.Controller;

import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Request.LoginRequest;
import com.springstudy.backend.Api.Auth.Model.Response.LoginResponse;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Response<User>> signin(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse
    , HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getCookies());
        return authService.signin(httpServletResponse, loginRequest);
    }
}
