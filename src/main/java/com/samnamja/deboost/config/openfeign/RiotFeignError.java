package com.samnamja.deboost.config.openfeign;

import com.samnamja.deboost.exception.custom.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class RiotFeignError implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                return CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("라이엇 API 오류 발생").build();
        }
        return null;
    }

}
