package com.samnamja.deboost.config.openfeign;

import com.samnamja.deboost.exception.custom.CustomException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;

public class RiotFeignError implements ErrorDecoder {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                byte[] responseBodyBytes = Util.toByteArray(response.body().asInputStream());
                String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);

                if (responseBody.contains("Data not found - summoner not found")) {
                    return CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("소환사를 찾을 수 없습니다.").build();
                } else {
                    return CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("라이엇 API 오류 발생").build();
                }
        }
        return null;
    }

}
