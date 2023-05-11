package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends CustomException{

    public ExpiredTokenException(String message) {
        super(message);
    }

    public ExpiredTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
