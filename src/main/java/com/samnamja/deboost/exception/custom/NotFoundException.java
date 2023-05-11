package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
