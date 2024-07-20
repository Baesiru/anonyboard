package com.example.anonyboard.controller;

import com.example.anonyboard.dto.EmailDto;
import com.example.anonyboard.service.EmailService;
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
    public ResponseEntity<Object> sendEmail(@RequestBody EmailDto emailDto){
        String mail = emailService.joinEmail(emailDto.getEmail());
        return ResponseEntity.ok().body("인증메일이 전송되었습니다.");
    }
}
