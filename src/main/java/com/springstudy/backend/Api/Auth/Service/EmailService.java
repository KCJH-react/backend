package com.springstudy.backend.Api.Auth.Service;

import com.springstudy.backend.Api.Auth.Model.Request.EmailRequest;
import com.springstudy.backend.Api.Auth.Model.Request.EmailVerifyRequest;
import com.springstudy.backend.Api.Repository.UserRepository;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.RedisService;
import com.springstudy.backend.Common.ResponseBuilder;
import com.springstudy.backend.Error;
import com.springstudy.backend.Response;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserRepository userRepository;

    @Autowired
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "verify0213@gmail.com";
    private int number;
    private final RedisService redisUtil;

    // 랜덤으로 숫자 생성
    //@Bean
    public static int createNumber() {
        return (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail) throws MessagingException {
        number = createNumber();
        redisUtil.setDataExpire(Integer.toString(number),mail,60*2);
        System.out.println(redisUtil.getData(Integer.toString(number))+" "+number);
        MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            System.out.println(mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");

        return message;
    }

    public ResponseEntity<Response<String>> sendMail(EmailRequest emailRequest) {
        try{
            MimeMessage message = CreateMail(emailRequest.email());
            javaMailSender.send(message);
        }
        catch(MessagingException e){
            return ResponseBuilder.<String>create()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(null)
                    .errorResponsev2(Error.BAD_REQUEST,"잘못된 이메일 형식")
                    .build();
        }
        catch(MailException e){
            return ResponseBuilder.<String>create()
                    .status(HttpStatus.BAD_GATEWAY)
                    .data(null)
                    .errorResponsev2(Error.INTERNAL_SERVER_ERROR,"인증 번호 전송 중 오류")
                    .build();
        }

        return ResponseBuilder.<String>create()
                .status(HttpStatus.OK)
                .data(null)
                .errorResponsev2(null,"인증 번호 발송")
                .build();
    }

    //추가 되었다.
    public ResponseEntity<Response<String>> CheckAuthNum(EmailVerifyRequest emailRequest){

        String authNum = emailRequest.authnum();
        String email = emailRequest.email();
        String storedEmail = redisUtil.getData(authNum);
        if(userRepository.findByEmail(email).isPresent()){
            return ResponseBuilder.<String>create()
                    .status(HttpStatus.OK)
                    .data(null)
                    .errorResponsev2(null,"이미 등록된 이메일")
                    .build();
        }

        if(storedEmail==null || !storedEmail.equals(email)){
            return ResponseBuilder.<String>create()
                    .status(HttpStatus.UNAUTHORIZED)
                    .data(null)
                    .errorResponsev2(Error.UNAUTHORIZED,"인증번호가 일치하지 않음")
                    .build();
        }
        return ResponseBuilder.<String>create()
                .status(HttpStatus.OK)
                .data(null)
                .errorResponsev2(null,"인증 성공")
                .build();
    }
}