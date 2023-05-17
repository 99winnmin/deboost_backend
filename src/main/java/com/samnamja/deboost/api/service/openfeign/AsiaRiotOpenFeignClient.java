package com.samnamja.deboost.api.service.openfeign;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import com.samnamja.deboost.config.openfeign.RiotOpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "asiaRiotOpenFeignClient", url = "https://asia.api.riotgames.com", configuration = RiotOpenFeignConfig.class)
public interface AsiaRiotOpenFeignClient {

    @GetMapping(value = "/lol/match/v5/matches/by-puuid/{puuid}/ids")
    List<GameIdResponseDto> getGameIds(@PathVariable("puuid") String puuid,
                                       @RequestHeader("X-Riot-Token") String riotToken,
                                       @RequestParam("type") String type,
                                       @RequestParam("start") int start,
                                       @RequestParam("count") int count);

    @GetMapping(value = "/lol/match/v5/matches/{matchId}")
    List<GameIdResponseDto> getGameDetailInfos(@PathVariable("matchId") String matchId,
                                       @RequestHeader("X-Riot-Token") String riotToken);
}
