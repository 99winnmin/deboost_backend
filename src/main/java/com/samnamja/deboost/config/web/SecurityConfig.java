package com.samnamja.deboost.config.web;

import com.samnamja.deboost.filter.jwt.JwtAuthenticationFilter;
import com.samnamja.deboost.filter.jwt.JwtExceptionFilter;
import com.samnamja.deboost.filter.jwt.JwtTokenUtil;
import com.samnamja.deboost.filter.logging.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 기본 웹보안 사용
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    private final CorsConfig corsConfig;

    public PasswordEncoder getPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui/**");
    }


    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .formLogin().disable();

        return httpSecurity
                .csrf().disable()// CSRF 설정 Disable
                .exceptionHandling()
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests() // http servletRequest 를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/profile", "/login/getGoogleAuthUrl","/login/oauth_google_check").permitAll()
                .antMatchers("/riot.txt").permitAll()
                .antMatchers("/fairy/device").permitAll()
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

                .and()
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtil, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .addFilterBefore(new LoggingFilter(), JwtExceptionFilter.class)

                .build();
    }
}