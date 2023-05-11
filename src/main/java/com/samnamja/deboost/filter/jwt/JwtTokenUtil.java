package com.samnamja.deboost.filter.jwt;

import com.samnamja.deboost.api.entity.user.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private static final int ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000;

    private static final int REFRESH_TOKEN_EXPIRATION_MS = 14 * 24 * 60 * 60 * 1000;

    // jwt 토큰 생성
    public String generateAccessToken(long userId, String email, Role role, boolean isActive) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_MS);
        Claims claims = Jwts.claims()
                .setSubject(email) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                ;
        claims.put("user_id", userId);
        claims.put("email", email);
        claims.put("role", role);
        claims.put("is_active", isActive);


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
    }

    public String generateRefreshToken(long userId, String email, Role role, boolean isActive) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_MS);
        Claims claims = Jwts.claims()
                .setSubject(email) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                ;

        claims.put("user_id", userId);
        claims.put("role", role);
        claims.put("is_active", isActive);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);

            boolean isActive = claims.getBody().get("is_active", Boolean.class);

            return isActive;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean isExpiredToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }


}
