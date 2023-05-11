package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class UnauthorizedTokenException extends CustomException{
    public UnauthorizedTokenException(String message) {
        super(message);
    }

    public UnauthorizedTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
