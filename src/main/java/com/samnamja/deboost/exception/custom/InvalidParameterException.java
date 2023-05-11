package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
