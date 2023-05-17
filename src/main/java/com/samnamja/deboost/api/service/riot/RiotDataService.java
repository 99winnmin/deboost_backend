package com.samnamja.deboost.api.service.riot;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerInfoResponseDto;
import com.samnamja.deboost.api.service.openfeign.RiotOpenFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiotDataService {
    @Value("${riot.api-key}")
    private String riotApiKey;

    private final RiotOpenFeignService riotOpenFeignService;

    public SummonerInfoResponseDto getSummonerId(){
        return riotOpenFeignService.getSummonerEncryptedId("", riotApiKey);
    }

    public List<SummonerDetailInfoResponseDto> getSummonerDetailInfo() {
        return riotOpenFeignService.getSummonerDetailInfo("", riotApiKey);
    }

    public List<GameIdResponseDto> getGameIds() {
        return riotOpenFeignService.getGameIds("", riotApiKey);
    }
}
