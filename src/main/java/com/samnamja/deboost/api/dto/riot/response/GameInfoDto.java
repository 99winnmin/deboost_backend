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
public class GameInfoDto {
    private String gameId;
    private ParticipantInfos participantInfos;
    private InfoDTO info;

    @Getter
    public static class ParticipantInfos {
        private List<ParticipantInfo> team1;
        private List<ParticipantInfo> team2;

        @Getter
        public static class ParticipantInfo {
            private Integer teamId;
            private String summonerName;
            private Integer championId;
            private String championName;

            @Builder
            public ParticipantInfo(Integer teamId, String summonerName, Integer championId, String championName) {
                this.teamId = teamId;
                this.summonerName = summonerName;
                this.championId = championId;
                this.championName = championName;
            }
        }

        @Builder
        public ParticipantInfos(List<ParticipantInfo> team1, List<ParticipantInfo> team2) {
            this.team1 = team1;
            this.team2 = team2;
        }

    }

    @Getter
    public static class InfoDTO {
        private Long gameEndTimestamp;
        private Long gameStartTimestamp;
        private GameData gameData;

        @Getter
        public static class GameData {
            private Integer championId;
            private String championName;
            private String summonerName;
            private Integer champLevel;
            // 스펠 1
            private Integer summoner1Id;
            // 스펠 2
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
            public GameData(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO participantDTO) {
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
        }

        @Builder
        public InfoDTO(Long gameEndTimestamp, Long gameStartTimestamp, GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO participants) {
            this.gameEndTimestamp = gameEndTimestamp;
            this.gameStartTimestamp = gameStartTimestamp;
            this.gameData = GameData.builder().participantDTO(participants).build();
        }
    }

    @Builder
    public GameInfoDto(String gameId, ParticipantInfos participantInfos, InfoDTO info) {
        this.gameId = gameId;
        this.participantInfos = participantInfos;
        this.info = info;
    }

    public static GameInfoDto from(GameAllDetailInfoResponseDto gameAllDetailInfoResponseDto, String desiredSummonerName) {
        GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO participantData = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .filter(participantDTO -> desiredSummonerName.equals(participantDTO.getSummonerName()))
                .collect(Collectors.toList()).get(0);

        Map<Integer, List<ParticipantInfos.ParticipantInfo>> team12 = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .map(participantDTO -> ParticipantInfos.ParticipantInfo.builder()
                        .teamId(participantDTO.getTeamId())
                        .summonerName(participantDTO.getSummonerName())
                        .championId(participantDTO.getChampionId())
                        .championName(participantDTO.getChampionName())
                        .build()
                )
                .collect(Collectors.groupingBy(ParticipantInfos.ParticipantInfo::getTeamId));

        return GameInfoDto.builder()
                .gameId(gameAllDetailInfoResponseDto.getMetadata().getMatchId())
                .participantInfos(ParticipantInfos.builder()
                        .team1(team12.get(100))
                        .team2(team12.get(200))
                        .build())
                .info(InfoDTO.builder()
                        .gameEndTimestamp(gameAllDetailInfoResponseDto.getInfo().getGameEndTimestamp())
                        .gameStartTimestamp(gameAllDetailInfoResponseDto.getInfo().getGameStartTimestamp())
                        .participants(participantData)
                        .build())
                .build();
    }
}
