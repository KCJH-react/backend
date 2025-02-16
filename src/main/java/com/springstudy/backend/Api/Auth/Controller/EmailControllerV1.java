package com.springstudy.backend.Api.Auth.Controller;

import com.springstudy.backend.Api.Auth.Model.Request.EmailRequest;
import com.springstudy.backend.Api.Auth.Service.EmailService;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/email")
@RequiredArgsConstructor
public class EmailControllerV1 {
    public final EmailService emailService;
    @PostMapping("/send")
    public ErrorCode sendEmail(@RequestParam @Valid String email) {
        return emailService.sendMail(email);
    }
    @PostMapping("/check")
    public ErrorCode checkEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return emailService.CheckAuthNum(emailRequest);
    }
}
