package com.samnamja.deboost.api.controller.user;

import com.samnamja.deboost.api.dto.user.UserInfoResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.user.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/my/info")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount) {
        UserInfoResponseDto myInfo = userService.getMyInfo(userAccount);
        return ResponseEntity.ok().body(myInfo);
    }
}
