package com.samnamja.deboost.api.controller.auth;

import com.samnamja.deboost.api.dto.auth.TokenResponseDto;
import com.samnamja.deboost.api.service.auth.GoogleOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoogleOAuthController {
    private final GoogleOAuthService googleOAuthService;

    // 구글 로그인창 호출
    @GetMapping(value = "/login/getGoogleAuthUrl")
    public ResponseEntity<?> getLoginUrl(HttpServletRequest request) throws Exception {
        String reqUrl = googleOAuthService.getGoogleAuthUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        //1.reqUrl 구글로그인 창을 띄우고, 로그인 후 /login/oauth_google_check 으로 리다이렉션하게 한다.
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 구글에서 리다이렉션
    @GetMapping(value = "/login/oauth_google_check")
    public ResponseEntity<TokenResponseDto> oauth_google_check(HttpServletRequest request,
                                                               @RequestParam(value = "code") String authCode,
                                                               HttpServletResponse response) throws Exception{

        TokenResponseDto tokenInfoDto = googleOAuthService.getGoogleUserInfo(authCode);
        return ResponseEntity.ok().body(tokenInfoDto);
    }
}
