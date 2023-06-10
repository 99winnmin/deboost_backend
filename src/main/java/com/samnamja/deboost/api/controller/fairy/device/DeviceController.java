package com.samnamja.deboost.api.controller.fairy.device;

import com.samnamja.deboost.api.dto.auth.TokenResponseDto;
import com.samnamja.deboost.api.dto.fairy.request.FairyAuthRequestDto;
import com.samnamja.deboost.api.service.fairy.auth.FairyAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DeviceController {
    private final FairyAuthService fairyAuthService;

    @PostMapping(value = "/fairy/device")
    public ResponseEntity<TokenResponseDto> registerDevice(@RequestBody FairyAuthRequestDto fairyAuthRequestDto) {
        TokenResponseDto tokenResponseDto = fairyAuthService.issuingFairyTokens(fairyAuthRequestDto);
        return ResponseEntity.ok().body(tokenResponseDto);
    }
}
