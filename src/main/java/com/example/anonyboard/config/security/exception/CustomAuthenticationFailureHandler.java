package com.example.anonyboard.config.security.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception)
            throws IOException {
        String error;
        String errorMessage = "아이디 또는 비밀번호를 확인해주세요.";
        if (exception instanceof IllegalUsernameException) {
            error = "username";
            errorMessage = exception.getMessage();
        }
        else if (exception instanceof IllegalPasswordException) {
            error = "password";
            errorMessage = exception.getMessage();
        }
        else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "사용자가 존재하지 않습니다.";
        }
        else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호를 확인해주세요.";
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
    }
}
