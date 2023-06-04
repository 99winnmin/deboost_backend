package com.samnamja.deboost.api.service.riot;

import com.samnamja.deboost.api.dto.openfeign.response.GameAllDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.riot.response.ManufactureResponseDto;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManuFactureDataService {

    public ManufactureResponseDto manufactureGameData(GameAllDetailInfoResponseDto gameAllDetailInfoResponseDto, String desiredSummonerName) {
        GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO ref = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .filter(participantDTO -> desiredSummonerName.equals(participantDTO.getSummonerName()))
                .collect(Collectors.toList()).get(0);
        // 게임이 유효한 게임인지 확인1
        int gameDuration = gameAllDetailInfoResponseDto.getInfo().getGameDuration() < 10000 ? gameAllDetailInfoResponseDto.getInfo().getGameDuration() : (int)gameAllDetailInfoResponseDto.getInfo().getGameDuration()/1000;
        if (gameDuration <= 600){
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("game is too short").build();
        }

        gameAllDetailInfoResponseDto.getInfo().getParticipants().forEach(participant -> {
            // 게임이 유효한 게임인지 확인2
            if (participant.getTeamPosition().equals("")){
                throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("game is not valid").build();
            }
        });

        // 필요 변수들 계산
        int gameDurationMinute = gameDuration / 60;
        int teamKill = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .filter(participantDTO -> participantDTO.getTeamId() == ref.getTeamId())
                .mapToInt(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO::getKills)
                .sum();
        int totalDeal = gameAllDetailInfoResponseDto.getInfo().getParticipants().stream()
                .filter(participantDTO -> participantDTO.getTeamId() == ref.getTeamId())
                .mapToInt(GameAllDetailInfoResponseDto.InfoDTO.ParticipantDTO::getTotalDamageDealtToChampions)
                .sum();


        int dtm = ((ref.getTotalDamageDealtToChampions()+ref.getTotalDamageTaken()))/gameDurationMinute;
        int dpm = ref.getTotalDamageDealtToChampions()/gameDurationMinute;
        double kap = teamKill == 0 ? 0 : Math.round((double) ref.getKills() / teamKill * 100 * 100) / 100.0;
        int vs = ref.getVisionScore();
        double dbgpm = Math.round((double) ref.getTotalDamageDealtToChampions() / (ref.getGoldEarned()/gameDurationMinute) * 100) / 100.0;
        double csm = Math.round((double) ref.getTotalMinionsKilled() / gameDurationMinute * 100) / 100.0;
        double gpm = Math.round((double) ref.getGoldEarned() / gameDurationMinute * 100) / 100.0;
        double dmgp = totalDeal == 0 ? 0 : Math.round((double) ref.getTotalDamageDealtToChampions() / totalDeal * 100 * 100) / 100.0;
        double vspm = Math.round((double) ref.getVisionScore() / gameDurationMinute * 100) / 100.0;
        double avgwpm = Math.round((double) ref.getWardsPlaced() / gameDurationMinute * 100) / 100.0;
        double avgwcpm = Math.round((double) ref.getWardsKilled() / gameDurationMinute * 100) / 100.0;
        double avgvwpm = Math.round((double) ref.getVisionWardsBoughtInGame() / gameDurationMinute * 100) / 100.0;
        // 2차 가공 데이터로 변환
        return ManufactureResponseDto.builder()
                .dtm(dtm).dpm(dpm).kap(kap).vs(vs).dbgpm(dbgpm).csm(csm)
                .gpm(gpm).dmgp(dmgp).vspm(vspm).avgwpm(avgwpm).avgvwpm(avgvwpm).avgwcpm(avgwcpm)
                .build();
    }

}
