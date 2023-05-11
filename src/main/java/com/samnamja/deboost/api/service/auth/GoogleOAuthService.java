package com.samnamja.deboost.api.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samnamja.deboost.api.dto.auth.GoogleLoginResponseDto;
import com.samnamja.deboost.api.dto.auth.GoogleOAuthRequestDto;
import com.samnamja.deboost.api.dto.auth.GoogleUserInfoDto;
import com.samnamja.deboost.api.dto.auth.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {
    @Value("${google.auth-url}")
    private String googleAuthUrl;

    @Value("${google.login-url}")
    private String googleLoginUrl;

    @Value("${google.redirect-url}")
    private String googleRedirectUrl;

    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    private final AuthService authService;

    public String getGoogleAuthUrl(){
        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    public TokenResponseDto getGoogleUserInfo(String code) throws JsonProcessingException {
        //2.구글에 등록된 레드망고 설정정보를 보내어 약속된 토큰을 받위한 객체 생성
        GoogleOAuthRequestDto googleOAuthRequest = GoogleOAuthRequestDto
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(code)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();

        RestTemplate restTemplate = new RestTemplate();

        //3.토큰요청을 한다.
        ResponseEntity<GoogleLoginResponseDto> apiResponse = restTemplate.postForEntity(googleAuthUrl + "/token", googleOAuthRequest, GoogleLoginResponseDto.class);
        //4.받은 토큰을 토큰객체에 저장
        GoogleLoginResponseDto googleLoginResponse = apiResponse.getBody();

        log.info("responseBody {}",googleLoginResponse.toString());


        String googleToken = googleLoginResponse.getId_token();

        //5.받은 토큰을 구글에 보내 유저정보를 얻는다.
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token",googleToken).toUriString();

        //6.허가된 토큰의 유저정보를 결과로 받는다.
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Object result = restTemplate.getForObject(requestUrl, Object.class);
        String userInfo = objectMapper.writeValueAsString(result);
        GoogleUserInfoDto googleUserInfoDto = objectMapper.readValue(userInfo, GoogleUserInfoDto.class);

        TokenResponseDto tokenResponseDto = authService.issuingTokens(googleUserInfoDto);

        return tokenResponseDto;
    }
}
