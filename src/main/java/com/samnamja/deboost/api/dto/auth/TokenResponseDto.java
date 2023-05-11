package com.samnamja.deboost.api.dto.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {

    private String accessToken;

    private String refreshToken;


    public static TokenResponseDto of(String accessToken, String refreshToken) {
        TokenResponseDto responseDto = new TokenResponseDto();
        responseDto.accessToken = accessToken;
        responseDto.refreshToken = refreshToken;

        return responseDto;
    }
}
