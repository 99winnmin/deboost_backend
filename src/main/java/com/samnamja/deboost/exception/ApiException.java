package com.samnamja.deboost.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class ApiException {

    private final String message;

    private final HttpStatus httpStatus;

    private final LocalDateTime timestamp;

}

