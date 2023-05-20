package com.samnamja.deboost.api.dto.openfeign.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerInfoResponseDto {
    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private int profileIconId;
//    private long revisionDate;
    private int summonerLevel;
}
