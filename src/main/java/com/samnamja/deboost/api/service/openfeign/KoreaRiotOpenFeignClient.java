package com.samnamja.deboost.api.service.openfeign;

import com.samnamja.deboost.api.dto.openfeign.response.SummonerDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerInfoResponseDto;
import com.samnamja.deboost.config.openfeign.RiotOpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "koreaRiotOpenFeignClient", url = "https://kr.api.riotgames.com", configuration = RiotOpenFeignConfig.class)
public interface KoreaRiotOpenFeignClient {

    @GetMapping(value = "/lol/summoner/v4/summoners/by-name/{summonerName}")
    SummonerInfoResponseDto getSummonerEncryptedId(@PathVariable("summonerName") String summonerName,
                                                   @RequestHeader("X-Riot-Token") String riotToken);

    @GetMapping(value = "/lol/league/v4/entries/by-summoner/{encryptedSummonerId}")
    List<SummonerDetailInfoResponseDto> getSummonerDetailInfo(@PathVariable("encryptedSummonerId") String encryptedSummonerId,
                                                              @RequestHeader("X-Riot-Token") String riotToken);
}
