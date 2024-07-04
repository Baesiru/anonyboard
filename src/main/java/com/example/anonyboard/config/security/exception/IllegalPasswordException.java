package com.example.anonyboard.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalPasswordException extends AuthenticationException {
    public IllegalPasswordException(String msg) {
        super(msg);
    }
}
