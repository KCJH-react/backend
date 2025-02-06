package com.springstudy.backend.Api.Auth.Controller;

import com.springstudy.backend.Api.Auth.Model.Request.CreateUserRequest;
import com.springstudy.backend.Api.Auth.Model.Response.CreateUserResponse;
import com.springstudy.backend.Api.Auth.Service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/user")
@RequiredArgsConstructor
public class CreateUserControllerV1 {
    private final CreateUserService createUserService;

    @PostMapping("/createUser")
    public CreateUserResponse CreateUser(@RequestBody CreateUserRequest createUserRequest) {
        return createUserService.createUser(createUserRequest);
    }
}
