package com.springstudy.backend.Api.Auth.Controller;
import com.springstudy.backend.Api.Auth.Model.Request.*;
import com.springstudy.backend.Api.Auth.Model.UserDTO;
import com.springstudy.backend.Api.Auth.Service.AuthService;
import com.springstudy.backend.Api.Auth.Service.EmailService;
import com.springstudy.backend.Api.Repository.Entity.User;
import com.springstudy.backend.Api.Repository.Entity.UserCategory;
import com.springstudy.backend.Common.Responsev2.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class AuthControllerV1 {
    private final AuthService authService;
    public final EmailService emailService;

    @GetMapping("")
    public ResponseEntity<Response<UserDTO>> get(@RequestParam Long userId) {
        return authService.get(userId);
    }
    @DeleteMapping("")
    public ResponseEntity<Response<UserDTO>> delete(@RequestParam Long userId) {
        return authService.delete(userId);
    }

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

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<String>> profileUpdate(
            @RequestPart MultipartFile profileImg,
            @PathVariable Long id){
        return authService.updateProfile(profileImg, id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Response<User>>  update(@RequestBody UpdateRequest updateRequest, @PathVariable Long id) {
        log.info("요청 password = {}", updateRequest.password());
        return authService.update(updateRequest, id);
    }

    @PostMapping("/send")
    public ResponseEntity<Response<String>> sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return emailService.sendMail(emailRequest);
    }
    @PostMapping("/check")
    public ResponseEntity<Response<String>> checkEmail(@RequestBody @Valid EmailVerifyRequest emailRequest) {
        return emailService.CheckAuthNum(emailRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response<Boolean>> logout(HttpServletRequest request) {
        return authService.logout(request);
    }


//    @GetMapping("/category/{id}")
//    public ResponseEntity<Response<List<UserCategory>>> getCategory(@PathVariable Long id) {
//        return authService.getCategory(id);
//    }
}
