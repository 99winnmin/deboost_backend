package com.samnamja.deboost.api.controller.riot;

import com.samnamja.deboost.api.dto.riot.request.SummonerSearchRequestDto;
import com.samnamja.deboost.api.dto.riot.response.GameSpecificDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.riot.response.SummonerSearchResponseDto;
import com.samnamja.deboost.api.dto.riot.response.TotalAnalysisResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.riot.RiotDataService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RiotController {
    private final RiotDataService riotDataService;

    // 1. GET 요청으로 검색 기록 있는지 확인 있으면 긁어서 줌 (상위 정보)
    @GetMapping(value = "/riot/gameinfo")
    public ResponseEntity<SummonerSearchResponseDto> getRecentGames
    (
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @RequestParam String summonerName,
            @Nullable @RequestParam Long cursor,
            @PageableDefault(size = 10) Pageable pageable
    )
    {
        SummonerSearchResponseDto summonerSearchResponseDto = riotDataService.get10GameData(summonerName, cursor, pageable);
        return ResponseEntity.ok().body(summonerSearchResponseDto);
    }

    // 2. GET 요청으로 한 게임 DETAIL 정보 리턴
    @GetMapping(value = "/riot/gameinfo/detail")
    public ResponseEntity<?> getSpecificGameData
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestParam String summonerName,
                    @RequestParam String gameId
            )
    {
        GameSpecificDetailInfoResponseDto detailGameData = riotDataService.getDetailGameData(gameId, summonerName);
        return ResponseEntity.ok().body(detailGameData);
    }

    // 3. POST 요청으로 최근 10게임 검색 기록 갱신 및 저장
    @PostMapping(value = "/riot/gameinfo")
    public ResponseEntity<Void> updateSummonerRecentGames
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody SummonerSearchRequestDto summonerSearchRequestDto
            )
    {
        riotDataService.updateGameData(summonerSearchRequestDto.getSummonerName());
        return ResponseEntity.ok().build();
    }

    // 4. PUT 요청 종합 분석 로직 실행
    @PostMapping(value = "/riot/gameinfo/analysis")
    public ResponseEntity<TotalAnalysisResponseDto> doTotalAnalysis
    (
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @RequestParam String summonerName
    )
    {
        riotDataService.analysisGameByFlask(summonerName);
        return ResponseEntity.ok().build();
    }

    // 5. GET 요청으로 종합 분석 결과 리턴
    @GetMapping(value = "/riot/gameinfo/analysis")
    public ResponseEntity<TotalAnalysisResponseDto> getTotalAnalysis
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestParam String summonerName
            )
    {
        TotalAnalysisResponseDto totalAnalysisResponseDto = riotDataService.analysisGameData(summonerName);
        return ResponseEntity.ok().body(totalAnalysisResponseDto);
    }

}
