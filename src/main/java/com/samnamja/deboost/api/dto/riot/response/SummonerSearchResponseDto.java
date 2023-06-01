package com.samnamja.deboost.api.dto.riot.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerSearchResponseDto {
    private SummonerInfo summonerInfo;
    // 한번이라도 검색된 적이 있냐?
    private boolean isSearchedBefore;
    private boolean isUpdated;
    private List<GameInfoDto> gameInfos;

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
    public SummonerSearchResponseDto(SummonerInfo summonerInfo, boolean isSearchedBefore, boolean isUpdated, List<GameInfoDto> gameInfos) {
        this.summonerInfo = summonerInfo;
        this.isSearchedBefore = isSearchedBefore;
        this.isUpdated = isUpdated;
        this.gameInfos = gameInfos;
    }
}
