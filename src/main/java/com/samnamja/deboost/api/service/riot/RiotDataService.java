package com.samnamja.deboost.api.service.riot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samnamja.deboost.api.dto.openfeign.response.GameAllDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.openfeign.response.SummonerInfoResponseDto;
import com.samnamja.deboost.api.dto.riot.response.GameInfoDto;
import com.samnamja.deboost.api.dto.riot.response.GameSpecificDetailInfoResponseDto;
import com.samnamja.deboost.api.dto.riot.response.SummonerSearchResponseDto;
import com.samnamja.deboost.api.entity.riot.AnalysisData.AnalysisData;
import com.samnamja.deboost.api.entity.riot.AnalysisData.AnalysisDataRepository;
import com.samnamja.deboost.api.entity.riot.UserHistory.UserHistory;
import com.samnamja.deboost.api.entity.riot.UserHistory.UserHistoryRepository;
import com.samnamja.deboost.api.service.openfeign.RiotOpenFeignService;
import com.samnamja.deboost.exception.custom.CustomException;
import com.samnamja.deboost.utils.aws.AmazonS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiotDataService {
    @Value("${riot.api-key}")
    private String riotApiKey;
    private final ObjectMapper objectMapper;
    private final RiotOpenFeignService riotOpenFeignService;
    private final ManuFactureDataService manuFactureDataService;
    private final AnalysisDataRepository analysisDataRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final AmazonS3Uploader amazonS3Uploader;

//    public SummonerSearchResponseDto get10GameData(String summonerName) {
//        AtomicReference<SummonerSearchResponseDto> summonerSearchResponseDto = null;
//        userHistoryRepository.findByHistoryGamerName(summonerName)
//                .ifPresentOrElse(userHistory -> {
//                    // 2. userHistory 에 존재하면 -> analysis data에서 10개 찾아서 데이터 구성해서 보내기
//                    List<GameInfoDto> gameInfoDtos = new ArrayList<>();
//                    SummonerInfoResponseDto summonerInfo = riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey);
//                    List<SummonerDetailInfoResponseDto> summonerDetailInfo = riotOpenFeignService.getSummonerDetailInfo(summonerInfo.getId(), riotApiKey);
//                    analysisDataRepository.findTop10ByUserHistory_IdOrderByCreatedAt(userHistory.getId())
//                            .stream().map(AnalysisData::getPrimaryDataUrl).collect(Collectors.toList())
//                            .forEach(url -> {
//                                GameAllDetailInfoResponseDto gameDetailInfos = amazonS3Uploader.loadJsonFileAndConvertToDto(url);
//                                gameInfoDtos.add(GameInfoDto.from(gameDetailInfos, summonerName));
//                            });
//                    summonerSearchResponseDto.set(SummonerSearchResponseDto.builder()
//                            .summonerInfo(SummonerSearchResponseDto.SummonerInfo.builder()
//                                    .summonerName(summonerInfo.getName())
//                                    .summonerLevel(summonerInfo.getSummonerLevel())
//                                    .summonerIconId(summonerInfo.getProfileIconId())
//                                    .tier(summonerDetailInfo.stream()
//                                            .filter(detailInfo -> detailInfo.getQueueType().equals("RANKED_SOLO_5x5"))
//                                            .findFirst().orElse(SummonerDetailInfoResponseDto.builder().tier("UNRANKED").build()).getTier()).build())
//                            .isSearchedBefore(true)
//                            .isUpdated(userHistory.getIsSearched())
//                            .gameInfos(gameInfoDtos).build());
//                }, () -> {
//                    // 1. userHistory 에 존재하는지 검색 -> 없으면 널값 보내주기
//                    summonerSearchResponseDto.set(SummonerSearchResponseDto.builder()
//                            .summonerInfo(SummonerSearchResponseDto.SummonerInfo.builder()
//                                    .summonerName(summonerName)
//                                    .summonerLevel(0)
//                                    .summonerIconId(0)
//                                    .tier("")
//                                    .build())
//                            .isSearchedBefore(false)
//                            .isUpdated(false)
//                            .gameInfos(null).build());
//                });
//        return summonerSearchResponseDto.get();
//    }

    public SummonerSearchResponseDto get10GameData(String summonerName) {
        return userHistoryRepository.findByHistoryGamerName(summonerName)
                .map(this::getExistingUserData)
                .orElseGet(() -> getNewUserData(summonerName));
    }

    private SummonerSearchResponseDto getExistingUserData(UserHistory userHistory) {
        String summonerName = userHistory.getHistoryGamerName();
        SummonerInfoResponseDto summonerInfo = riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey);
        List<SummonerDetailInfoResponseDto> summonerDetailInfo = riotOpenFeignService.getSummonerDetailInfo(summonerInfo.getId(), riotApiKey);
        List<GameInfoDto> gameInfoDtos = getGameInfoDtos(userHistory, summonerName);

        return SummonerSearchResponseDto.builder()
                .summonerInfo(buildSummonerInfo(summonerInfo, summonerDetailInfo))
                .isSearchedBefore(true)
                .isUpdated(userHistory.getIsSearched())
                .gameInfos(gameInfoDtos)
                .build();
    }

    private List<GameInfoDto> getGameInfoDtos(UserHistory userHistory, String summonerName) {
        return analysisDataRepository.findTop10ByUserHistory_IdOrderByCreatedAtDesc(userHistory.getId())
                .stream()
                .map(AnalysisData::getPrimaryDataUrl)
                .map(amazonS3Uploader::loadJsonFileAndConvertToDto)
                .map(gameDetailInfos -> GameInfoDto.from(gameDetailInfos, summonerName))
                .collect(Collectors.toList());
    }

    private SummonerSearchResponseDto getNewUserData(String summonerName) {
        return SummonerSearchResponseDto.builder()
                .summonerInfo(buildEmptySummonerInfo(summonerName))
                .isSearchedBefore(false)
                .isUpdated(false)
                .gameInfos(null)
                .build();
    }

    private SummonerSearchResponseDto.SummonerInfo buildSummonerInfo(SummonerInfoResponseDto summonerInfo, List<SummonerDetailInfoResponseDto> summonerDetailInfo) {
        String tier = summonerDetailInfo.stream()
                .filter(detailInfo -> detailInfo.getQueueType().equals("RANKED_SOLO_5x5"))
                .findFirst()
                .map(SummonerDetailInfoResponseDto::getTier)
                .orElse("UNRANKED");

        return SummonerSearchResponseDto.SummonerInfo.builder()
                .summonerName(summonerInfo.getName())
                .summonerLevel(summonerInfo.getSummonerLevel())
                .summonerIconId(summonerInfo.getProfileIconId())
                .tier(tier)
                .build();
    }

    private SummonerSearchResponseDto.SummonerInfo buildEmptySummonerInfo(String summonerName) {
        return SummonerSearchResponseDto.SummonerInfo.builder()
                .summonerName(summonerName)
                .summonerLevel(0)
                .summonerIconId(0)
                .tier("")
                .build();
    }


    public GameSpecificDetailInfoResponseDto getDetailGameData(String gameId, String summonerName) {
        UserHistory userHistory = userHistoryRepository.findByHistoryGamerName(summonerName)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사의 검색 기록이 없습니다. summonerName=" + summonerName).build());
        String primaryDataUrl = analysisDataRepository.findAnalysisDataByGameIdAndUserHistory_Id(gameId, userHistory.getId()).getPrimaryDataUrl();
        GameAllDetailInfoResponseDto gameAllDetailInfoResponseDto = amazonS3Uploader.loadJsonFileAndConvertToDto(primaryDataUrl);
        return GameSpecificDetailInfoResponseDto.from(gameAllDetailInfoResponseDto, summonerName);
    }

    @Async
    @Transactional
    public void updateGameData(String summonerName){
        // RIOT API 호출
        SummonerInfoResponseDto summonerInfo = riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey);
        List<GameIdResponseDto> gameIds = riotOpenFeignService.getGameIds(summonerInfo.getPuuid(), riotApiKey);
        gameIds.sort(Comparator.comparing(GameIdResponseDto::getGameId));

        UserHistory userHistory = userHistoryRepository.findByHistoryGamerName(summonerName)
                .map(existingUserHistory -> {
                    existingUserHistory.updateIsSearched();
                    return existingUserHistory;
                })
                .orElseGet(() -> {
                    UserHistory newUserHistory = UserHistory.builder()
                            .historyGamerName(summonerName)
                            .isSearched(false)
                            .build();
                    return userHistoryRepository.save(newUserHistory);
                });

        // DB에 저장된 데이터와 비교 : API 로 불러온 gameId 중에 DB에 저장된 gameId에 해당하는 것들을 호출
        List<AnalysisData> existsAnalysisData = analysisDataRepository.findAnalysisDataBySummonerNameAndGameIds(summonerName, gameIds);

        // 새로 추가된 게임이 있는 경우 -> API 호출해서 GameSpecificDetailInfoResponseDto 으로 load
        List<GameAllDetailInfoResponseDto> newSearchGamesDetailInfos = gameIds.stream().map(GameIdResponseDto::getGameId).collect(Collectors.toList()).stream()
                .filter(gameId -> !existsAnalysisData.stream().map(AnalysisData::getGameId).collect(Collectors.toList()).contains(gameId))
                .collect(Collectors.toList()).stream()
                .map(gameId -> riotOpenFeignService.getGameDetailInfos(gameId, riotApiKey)).collect(Collectors.toList());

        // S3 저장
        newSearchGamesDetailInfos.stream().forEach(gameDetailInfos -> {
            String jsonString = null;
            try {
                jsonString = objectMapper.writeValueAsString(gameDetailInfos);
            } catch (JsonProcessingException e) {
                throw CustomException.builder().message("dto to json convert failed").build();
            }
            String jsonFileS3Key = amazonS3Uploader.saveJsonStringAndGetKey(jsonString, summonerName);
            AnalysisData analysisData = AnalysisData.builder()
                    .gameId(gameDetailInfos.getMetadata().getMatchId())
                    .userHistory(userHistory)
                    .primaryDataUrl(jsonFileS3Key)
                    .build();
            analysisDataRepository.save(analysisData);
        });
    }

}
