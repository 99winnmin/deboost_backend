package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException{
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
