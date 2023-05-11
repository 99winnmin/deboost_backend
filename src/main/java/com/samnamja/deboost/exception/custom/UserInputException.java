package com.samnamja.deboost.exception.custom;

import org.springframework.http.HttpStatus;

public class UserInputException extends CustomException {
    public UserInputException(String message) {
        super(message);
    }

    public UserInputException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
