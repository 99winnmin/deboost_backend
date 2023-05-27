package com.samnamja.deboost.config.openfeign;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.samnamja.deboost.api.service.openfeign")
public class RiotOpenFeignConfig {

    @Bean
    Logger.Level riotFeignClientLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new RiotFeignError();
    }
}
