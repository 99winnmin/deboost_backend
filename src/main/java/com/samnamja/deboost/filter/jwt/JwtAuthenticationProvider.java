package com.samnamja.deboost.filter.jwt;

import com.samnamja.deboost.api.entity.user.detail.UserAccountService;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserAccountService userAccountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null || !jwtTokenUtil.isValidToken(authentication.getPrincipal().toString())) {

            throw CustomException.builder().httpStatus(HttpStatus.UNAUTHORIZED).message("인증되지 않은 사용자입니다.").build();
        }


        UserDetails userDetails =  userAccountService.loadUserByUsername((String) authentication.getPrincipal());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
