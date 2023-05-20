package com.samnamja.deboost.api.controller.riot;

import com.samnamja.deboost.api.dto.riot.request.SummonerSearchRequestDto;
import com.samnamja.deboost.api.dto.riot.response.SummonerSearchResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.riot.RiotDataService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RiotController {
    private final RiotDataService riotDataService;

    @PostMapping(value = "/riot/summoner")
    public ResponseEntity<SummonerSearchResponseDto> analysisSummoner
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody SummonerSearchRequestDto summonerSearchRequestDto
            )
    {
        SummonerSearchResponseDto summonerSearchResponseDto = riotDataService.analysisSummoner(summonerSearchRequestDto.getSummonerName());
        return ResponseEntity.ok().body(summonerSearchResponseDto);
    }

}
