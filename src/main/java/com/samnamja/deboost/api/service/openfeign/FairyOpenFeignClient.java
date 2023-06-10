package com.samnamja.deboost.api.service.openfeign;

import com.samnamja.deboost.api.dto.fairy.request.FairyRequestDto;
import com.samnamja.deboost.api.dto.fairy.response.ChatCompletionDto;
import com.samnamja.deboost.config.openfeign.RiotOpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "fairyOpenFeignClient", url = "https://api.openai.com", configuration = RiotOpenFeignConfig.class)
public interface FairyOpenFeignClient {

    @PostMapping(value = "/v1/chat/completions")
    ChatCompletionDto getFairyContent(@RequestHeader("Authorization") String openaiKey, @RequestBody FairyRequestDto fairyRequestDto);
}