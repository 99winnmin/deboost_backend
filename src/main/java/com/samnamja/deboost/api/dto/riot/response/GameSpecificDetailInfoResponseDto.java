package com.samnamja.deboost.api.dto.riot.response;

import com.samnamja.deboost.api.dto.openfeign.response.GameAllDetailInfoResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameSpecificDetailInfoResponseDto {
    private String gameId;
    private GameInfo gameInfo;
    private List<SummonerData> team1;
    private List<SummonerData> team2;
    private ManufactureResponseDto manufactureInfo;

    @Getter
    public static class SummonerData {
        private Integer championId;
        private String championName;
        private String summonerName;
        private Integer champLevel;
        private Integer summoner1Id;
        private Integer summoner2Id;
        private Integer kills;
        private Integer deaths;
        private Integer assists;
        private Integer item0;
        private Integer item1;
        private Integer item2;
        private Integer item3;
        private Integer item4;
        private Integer item5;
        private Integer item6;
        private Integer totalMinionsKilled;
        private Integer goldEarned;
        private Integer dragonKills;
        private Integer baronKills;
        private boolean win;

        @Builder
        public SummonerData(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO participantDTO) {
            this.championId = participantDTO.getChampionId();
            this.championName = participantDTO.getChampionName();
            this.summonerName = participantDTO.getSummonerName();
            this.champLevel = participantDTO.getChampLevel();
            this.summoner1Id = participantDTO.getSummoner1Id();
            this.summoner2Id = participantDTO.getSummoner2Id();
            this.kills = participantDTO.getKills();
            this.deaths = participantDTO.getDeaths();
            this.assists = participantDTO.getAssists();
            this.item0 = participantDTO.getItem0();
            this.item1 = participantDTO.getItem1();
            this.item2 = participantDTO.getItem2();
            this.item3 = participantDTO.getItem3();
            this.item4 = participantDTO.getItem4();
            this.item5 = participantDTO.getItem5();
            this.item6 = participantDTO.getItem6();
            this.totalMinionsKilled = participantDTO.getTotalMinionsKilled();
            this.goldEarned = participantDTO.getGoldEarned();
            this.dragonKills = participantDTO.getDragonKills();
            this.baronKills = participantDTO.getBaronKills();
            this.win = participantDTO.isWin();
        }

        public static SummonerData from(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO participantDTO) {
            return SummonerData.builder().participantDTO(participantDTO).build();
        }

    }

    @Getter
    public static class GameInfo {
        private int gameDuration;
        private Long gameStartTimestamp;

        @Builder
        public GameInfo(int gameDuration, Long gameStartTimestamp) {
            this.gameDuration = gameDuration;
            this.gameStartTimestamp = gameStartTimestamp;
        }

        public static GameInfo from(GameAllDetailInfoResponseDto.InfoDTO infoDTO) {
            return GameInfo.builder()
                    .gameDuration(infoDTO.getGameDuration())
                    .gameStartTimestamp(infoDTO.getGameStartTimestamp())
                    .build();
        }
    }

    @Builder
    public GameSpecificDetailInfoResponseDto(String gameId, GameInfo gameInfo, List<SummonerData> team1, List<SummonerData> team2, ManufactureResponseDto manufactureInfo) {
        this.gameId = gameId;
        this.gameInfo = gameInfo;
        this.team1 = team1;
        this.team2 = team2;
        this.manufactureInfo = manufactureInfo;
    }


    public static GameSpecificDetailInfoResponseDto from(GameAllDetailInfoResponseDto gameAllDetailInfoResponseDto, String desiredSummonerName, ManufactureResponseDto manufactureResponseDto) {
        Map<Boolean, List<GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO>> team1AndTeam2 = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .collect(Collectors.partitioningBy(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO::isWin));
        return GameSpecificDetailInfoResponseDto.builder()
                .gameId(gameAllDetailInfoResponseDto.getMetadata().getMatchId())
                .gameInfo(GameInfo.from(gameAllDetailInfoResponseDto.getInfo()))
                .team1(team1AndTeam2.get(true).stream().map(SummonerData::from).collect(Collectors.toList()))
                .team2(team1AndTeam2.get(false).stream().map(SummonerData::from).collect(Collectors.toList()))
                .manufactureInfo(manufactureResponseDto)
                .build();
    }
}
