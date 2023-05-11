package com.samnamja.deboost.filter.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.resolveToken(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(token, "");

        Authentication authenticatedAuthentication = authenticationManager.authenticate(authentication);
        if (authenticatedAuthentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedAuthentication);
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/login/getGoogleAuthUrl", "/login/oauth_google_check", "/profile"
        }; // TODO: JWT 안쓰는 API 추가 필요
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}