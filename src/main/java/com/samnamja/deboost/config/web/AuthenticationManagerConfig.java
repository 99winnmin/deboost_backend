package com.samnamja.deboost.config.web;

import com.samnamja.deboost.filter.jwt.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@RequiredArgsConstructor
@Configuration
public class AuthenticationManagerConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final AuthenticationConfiguration authenticationConfiguration;
    /*
     * AuthenticationManager를 주입받기 위해서 빈으로 등록한다.
     * */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return new ProviderManager(jwtAuthenticationProvider);
    }
}
