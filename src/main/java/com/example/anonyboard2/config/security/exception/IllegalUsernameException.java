package com.example.anonyboard2.config.security.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalUsernameException extends AuthenticationException {
    public IllegalUsernameException(String msg) {
        super(msg);
    }
}
