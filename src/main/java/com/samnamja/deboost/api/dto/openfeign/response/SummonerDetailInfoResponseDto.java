package com.samnamja.deboost.api.dto.openfeign.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SummonerDetailInfoResponseDto {
//    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
//    private String summonerId;
//    private String summonerName;
    private int leaguePoints;
    private int wins;
    private int losses;
//    private boolean veteran;
//    private boolean inactive;
//    private boolean freshBlood;
//    private boolean hotStreak;
}
