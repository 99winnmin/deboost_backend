package com.samnamja.deboost.exception.handler;

import com.samnamja.deboost.exception.ApiException;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ApiException> handlerCustomException(CustomException e) {
        log.error("Status: {}, Message: {}", e.getHttpStatus(), e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiException
                        .builder()
                        .message(e.getMessage())
                        .httpStatus(e.getHttpStatus())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
