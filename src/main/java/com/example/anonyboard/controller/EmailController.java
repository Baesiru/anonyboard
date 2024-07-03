package com.example.anonyboard.controller;

import com.example.anonyboard.dto.EmailDto;
import com.example.anonyboard.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/api/send")
    public ResponseEntity<Object> sendEmail(@RequestBody EmailDto emailDto) throws MessagingException {
        emailService.sendEmail(emailDto.getEmail());
        return ResponseEntity.ok().body("이메일이 전송되었습니다.");
    }

    @PostMapping("/api/verify")
    public String verify(EmailDto emailDto) {
        boolean isVerify = emailService.verifyEmailCode(emailDto.getEmail(), emailDto.getCode());
        return isVerify ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }
}
