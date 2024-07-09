package com.example.anonyboard.config.security.filter;

import com.example.anonyboard.config.security.exception.IllegalPasswordException;
import com.example.anonyboard.config.security.exception.IllegalUsernameException;
import com.example.anonyboard.exception.errorCode.CommonErrorCode;
import com.example.anonyboard.exception.errorCode.ErrorCode;
import com.example.anonyboard.exception.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    public ExceptionHandlerFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e){
            if (e instanceof BadCredentialsException) {
                handleErrorResponse(response, CommonErrorCode.BAD_CREDENTIALS);
            }
            else if(e instanceof IllegalUsernameException) {
                handleErrorResponse(response, CommonErrorCode.INVALID_USERNAME_PARAMETER);
            }
            else if(e instanceof IllegalPasswordException) {
                handleErrorResponse(response, CommonErrorCode.INVALID_PASSWORD_PARAMETER);
            }
            else {
                handleErrorResponse(response, CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode){
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private void handleErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException, ServletException{
        ErrorResponse errorResponse = makeErrorResponse(errorCode);
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }



}
