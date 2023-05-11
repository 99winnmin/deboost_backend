package com.samnamja.deboost.filter.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (CustomException e) {
            final Map<String, Object> body = new HashMap<>();
            final ObjectMapper mapper = new ObjectMapper();
            response.setStatus(e.getHttpStatus().value());
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("httpStatus", HttpStatus.UNAUTHORIZED);
            body.put("message", "인증되지 않은 사용자입니다.");
            mapper.writeValue(response.getOutputStream(), body);
        }
    }
}
