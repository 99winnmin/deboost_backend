package com.samnamja.deboost.api.dto.riot.response;

import com.samnamja.deboost.api.dto.openfeign.response.GameSpecificDetailInfoResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerSearchResponseDto {
    private SummonerInfo summonerInfo;
    private List<GameSpecificDetailInfoResponseDto> gameInfos;

    @Getter
    public static class SummonerInfo {
        private String summonerName;
        private int summonerLevel;
        private int summonerIconId;
        private String tier;

        @Builder
        public SummonerInfo(String summonerName, int summonerLevel, int summonerIconId, String tier) {
            this.summonerName = summonerName;
            this.summonerLevel = summonerLevel;
            this.summonerIconId = summonerIconId;
            this.tier = tier;
        }
    }

    @Builder
    public SummonerSearchResponseDto(SummonerInfo summonerInfo, List<GameSpecificDetailInfoResponseDto> gameInfos) {
        this.summonerInfo = summonerInfo;
        this.gameInfos = gameInfos;
    }
}
