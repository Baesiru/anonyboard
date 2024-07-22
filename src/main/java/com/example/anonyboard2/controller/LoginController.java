package com.example.anonyboard2.controller;

import com.example.anonyboard2.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletResponse response){
        ResponseEntity<Object> checkLogout = loginService.logout(response);
        return checkLogout;
    }
}
