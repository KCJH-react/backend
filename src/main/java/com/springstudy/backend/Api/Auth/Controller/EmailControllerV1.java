package com.springstudy.backend.Api.Auth.Controller;

import com.springstudy.backend.Api.Auth.Model.Request.EmailRequest;
import com.springstudy.backend.Api.Auth.Model.Request.EmailVerifyRequest;
import com.springstudy.backend.Api.Auth.Service.EmailService;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("api/v1/email")
@RequiredArgsConstructor
public class EmailControllerV1 {
    public final EmailService emailService;
    @PostMapping("/send")
    public ErrorCode sendEmail(@RequestBody @Valid EmailRequest emailRequest) {
        return emailService.sendMail(emailRequest);
    }
    @PostMapping("/check")
    public ErrorCode checkEmail(@RequestBody @Valid EmailVerifyRequest emailRequest) {
        return emailService.CheckAuthNum(emailRequest);
    }
}