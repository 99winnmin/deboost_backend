package com.samnamja.deboost.api.service.openfeign;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiotOpenFeignService{
    private final KoreaRiotOpenFeignClient koreaRiotOpenFeignClient;
    private final AsiaRiotOpenFeignClient asiaRiotOpenFeignClient;

    public SummonerInfoResponseDto getSummonerEncryptedId(String summonerName, String riotApiKey) {
        return koreaRiotOpenFeignClient.getSummonerEncryptedId(summonerName, riotApiKey);
    }

    public List<SummonerDetailInfoResponseDto> getSummonerDetailInfo(String encryptedSummonerId, String riotToken) {
        return koreaRiotOpenFeignClient.getSummonerDetailInfo(encryptedSummonerId, riotToken);
    }

    public List<GameIdResponseDto> getGameIds(String puuid, String riotToken) {
        return asiaRiotOpenFeignClient.getGameIds(puuid, riotToken, "ranked", 0, 10);
    }
}
