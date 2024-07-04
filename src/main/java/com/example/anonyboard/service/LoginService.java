package com.example.anonyboard.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    public ResponseEntity<Object> logout(HttpServletResponse response) {
        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        refreshToken.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(refreshToken);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
