package com.samnamja.deboost.api.controller.riot;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerInfoResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.riot.RiotDataService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RiotController {
    private final RiotDataService riotDataService;

    @GetMapping(value = "/test")
    public ResponseEntity<?> test
            (
                @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
//                @RequestParam("name") String name
            )
    {
        SummonerInfoResponseDto responseDto = riotDataService.getSummonerId();
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/test2")
    public ResponseEntity<?> test2
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
//                @RequestParam("name") String name
            )
    {
        List<SummonerDetailInfoResponseDto> responseDto = riotDataService.getSummonerDetailInfo();
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping(value = "/test3")
    public ResponseEntity<?> test3
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
//                @RequestParam("name") String name
            )
    {
        List<GameIdResponseDto> responseDto = riotDataService.getGameIds();
        return ResponseEntity.ok().body(responseDto);
    }
}
