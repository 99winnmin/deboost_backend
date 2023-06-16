package com.samnamja.deboost.api.service.riot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samnamja.deboost.api.dto.openfeign.request.FlaskRequestDto;
import com.samnamja.deboost.api.dto.openfeign.response.*;
import com.samnamja.deboost.api.dto.riot.response.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiotDataService {
    @Value("${riot.api-key}")
    private String riotApiKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final ObjectMapper objectMapper;
    private final RiotOpenFeignService riotOpenFeignService;
    private final FlaskService flaskService;
    private final ManuFactureDataService manuFactureDataService;
    private final AnalysisDataRepository analysisDataRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final AmazonS3Uploader amazonS3Uploader;


    public SummonerSearchResponseDto get10GameData(String summonerName, Long cursor, Pageable pageable) {
        Optional.of(riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey))
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사가 없습니다. summonerName = " + summonerName).build());

        return userHistoryRepository.findByHistoryGamerName(summonerName)
                .map(userHistory -> getExistingUserData(userHistory, cursor, pageable))
                .orElseGet(() -> getNewUserData(summonerName));
    }

    private SummonerSearchResponseDto getExistingUserData(UserHistory userHistory, Long cursor, Pageable pageable) {
        String summonerName = userHistory.getHistoryGamerName();
        SummonerInfoResponseDto summonerInfo = riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey);
        List<SummonerDetailInfoResponseDto> summonerDetailInfo = riotOpenFeignService.getSummonerDetailInfo(summonerInfo.getId(), riotApiKey);
        List<GameInfoDto> gameInfoDtos = getGameInfoDtos(userHistory, summonerName, cursor, pageable);

        return SummonerSearchResponseDto.builder()
                .summonerInfo(buildSummonerInfo(summonerInfo, summonerDetailInfo))
                .isSearchedBefore(true)
                .isUpdated(userHistory.getIsSearched())
                .gameInfos(gameInfoDtos)
                .build();
    }

    private List<GameInfoDto> getGameInfoDtos(UserHistory userHistory, String summonerName, Long cursor, Pageable pageable) {
        return analysisDataRepository.findTop10ByUserHistory_IdAndAnalysisIdOrderByCreatedAtDesc(userHistory.getId(), cursor, pageable)
                .stream()
                .map(analysisData -> analysisData.getPrimaryDataUrlAndId(analysisData))
                .map(primaryDataUrlAndId -> GameInfoDto.from(amazonS3Uploader.loadJsonFileAndConvertToDto(primaryDataUrlAndId.getPrimaryDataUrl()), summonerName, primaryDataUrlAndId.getPrimaryDataId()))
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
        SummonerDetailInfoResponseDto.TierData tierData = summonerDetailInfo.stream()
                .filter(detailInfo -> detailInfo.getQueueType().equals("RANKED_SOLO_5x5"))
                .findFirst()
//                .map(SummonerDetailInfoResponseDto::getTierData)
                .map(detailInfo -> detailInfo.getTierData(detailInfo.getTier(), detailInfo.getRank()))
                .orElse(null);

        String strRank = Optional.of(Objects.requireNonNull(tierData).getRank())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("Rank 정보가 없습니다.").build());

        int tier = 0;
        for (int i=0 ; i < strRank.length() ; i++){
            char currentChar = strRank.charAt(i);
            switch (currentChar) {
                case 'I':
                    tier += 1;
                    break;
                case 'V':
                    tier += 5;
                    break;
                default:
                    break;
            }
        }

        return SummonerSearchResponseDto.SummonerInfo.builder()
                .summonerName(summonerInfo.getName())
                .summonerLevel(summonerInfo.getSummonerLevel())
                .summonerIconId(summonerInfo.getProfileIconId())
                .tier(tierData.getTier())
                .rank(tier)
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
        ManufactureResponseDto manufactureResponseDto = manuFactureDataService.manufactureGameData(gameAllDetailInfoResponseDto, summonerName);
        return GameSpecificDetailInfoResponseDto.from(gameAllDetailInfoResponseDto, summonerName, manufactureResponseDto);
    }

    @Async
    @Transactional
    public void updateGameData(String summonerName){
        SummonerInfoResponseDto summonerInfo = Optional.of(riotOpenFeignService.getSummonerEncryptedId(summonerName, riotApiKey))
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사가 없습니다. summonerName = " + summonerName).build());

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


    public TotalAnalysisResponseDto analysisGameData(String summonerName){
        UserHistory userHistory = userHistoryRepository.findByHistoryGamerName(summonerName)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사의 검색 기록이 없습니다. summonerName=" + summonerName).build());

        List<AnalysisData> data = analysisDataRepository.findTop10ByUserHistory_IdOrderByCreatedAtDesc(userHistory.getId());
        if (data.stream().map(AnalysisData::getModelPrediction).anyMatch(Objects::isNull)){
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("해당 소환사의 최근 10게임 중 분석이 완료되지 않은 게임이 있습니다.").build();
        }

//        DoubleSummaryStatistics dtmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics dpmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics kapStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics vsStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics dbgpmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics csmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics gpmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics dmgpStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics vspmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics avgwpmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics avgwcpmStats = new DoubleSummaryStatistics();
//        DoubleSummaryStatistics avgvwpmStats = new DoubleSummaryStatistics();
//
//        data.stream()
//                .map(AnalysisData::getPrimaryDataUrl)
//                .map(amazonS3Uploader::loadJsonFileAndConvertToDto)
//                .map(totalData -> manuFactureDataService.manufactureGameData(totalData, summonerName))
//                .forEach(manufactureResponseDto -> {
//                    dtmStats.accept(manufactureResponseDto.getDtm());
//                    dpmStats.accept(manufactureResponseDto.getDpm());
//                    kapStats.accept(manufactureResponseDto.getKap());
//                    vsStats.accept(manufactureResponseDto.getVs());
//                    dbgpmStats.accept(manufactureResponseDto.getDbgpm());
//                    csmStats.accept(manufactureResponseDto.getCsm());
//                    gpmStats.accept(manufactureResponseDto.getGpm());
//                    dmgpStats.accept(manufactureResponseDto.getDmgp());
//                    vspmStats.accept(manufactureResponseDto.getVspm());
//                    avgwpmStats.accept(manufactureResponseDto.getAvgwpm());
//                    avgwcpmStats.accept(manufactureResponseDto.getAvgwcpm());
//                    avgvwpmStats.accept(manufactureResponseDto.getAvgvwpm());
//                });
//        ManufactureAverageResponseDto manufactureAverageResponseDto = ManufactureAverageResponseDto.builder()
//                .dtm(dtmStats.getAverage())
//                .dpm(dpmStats.getAverage())
//                .kap(kapStats.getAverage())
//                .vs(vsStats.getAverage())
//                .dbgpm(dbgpmStats.getAverage())
//                .csm(csmStats.getAverage())
//                .gpm(gpmStats.getAverage())
//                .dmgp(dmgpStats.getAverage())
//                .vspm(vspmStats.getAverage())
//                .avgwpm(avgwpmStats.getAverage())
//                .avgwcpm(avgwcpmStats.getAverage())
//                .avgvwpm(avgvwpmStats.getAverage())
//                .build();

        List<ManufactureResponseDto> manufactureCollect = data.stream()
                .map(AnalysisData::getPrimaryDataUrl)
                .map(amazonS3Uploader::loadJsonFileAndConvertToDto)
                .map(totalData -> manuFactureDataService.manufactureGameData(totalData, summonerName)).collect(Collectors.toList());

        ManufactureAverageResponseDto manufactureAverageResponseDto = ManufactureAverageResponseDto.builder()
                .dtm(manufactureCollect.stream().mapToInt(ManufactureResponseDto::getDtm).average().orElse(0))
                .dpm(manufactureCollect.stream().mapToInt(ManufactureResponseDto::getDpm).average().orElse(0))
                .kap(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getKap).average().orElse(0))
                .vs(manufactureCollect.stream().mapToInt(ManufactureResponseDto::getVs).average().orElse(0))
                .dbgpm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getDbgpm).average().orElse(0))
                .csm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getCsm).average().orElse(0))
                .gpm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getGpm).average().orElse(0))
                .dmgp(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getDmgp).average().orElse(0))
                .vspm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getVspm).average().orElse(0))
                .avgwpm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getAvgwpm).average().orElse(0))
                .avgwcpm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getAvgwcpm).average().orElse(0))
                .avgvwpm(manufactureCollect.stream().mapToDouble(ManufactureResponseDto::getAvgvwpm).average().orElse(0))
                .build();


        // 뒤에서부터 최근 게임
        List<Double> predictionList = data.stream().map(AnalysisData::getModelPrediction).sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        return TotalAnalysisResponseDto.builder()
                .manufactureInfo(manufactureAverageResponseDto)
                .predictionList(predictionList)
                .build();
    }

    @Async
    @Transactional
    public void analysisGameByFlask(String summonerName){ // 아직 매우 미완성
        UserHistory userHistory = userHistoryRepository.findByHistoryGamerName(summonerName)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사의 검색 기록이 없습니다. summonerName=" + summonerName).build());

        analysisDataRepository.findTop10ByUserHistory_IdOrderByCreatedAtDesc(userHistory.getId())
                .stream()
                .filter(analysisData -> analysisData.getModelPrediction() != null) // null 값이 아닌 것만 분석하도록
                .map(AnalysisData::getPrimaryDataUrl).collect(Collectors.toList()).stream()
                .forEach(jsonKey -> {
                    FlaskResponseDto flaskResponseDto = flaskService.analysisGameData(FlaskRequestDto.builder().bucketName(bucketName).keyName(jsonKey).build());
                    analysisDataRepository.updateAnalysisDataPrediction(flaskResponseDto.getData().get(0).get(0), LocalDateTime.now(), jsonKey);
                });
    }
}
