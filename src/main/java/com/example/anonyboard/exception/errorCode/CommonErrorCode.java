package com.example.anonyboard.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "Required parameter missing"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 확인해주세요"),
    INVALID_USERNAME_PARAMETER(HttpStatus.BAD_REQUEST, "아이디를 입력해주세요"),
    INVALID_PASSWORD_PARAMETER(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
