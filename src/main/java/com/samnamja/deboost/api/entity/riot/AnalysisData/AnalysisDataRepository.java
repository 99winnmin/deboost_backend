package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisDataRepository extends JpaRepository<AnalysisData, Long>, AnalysisDataRepositoryCustom {
    List<AnalysisData> findAnalysisDataBySummonerNameAndGameIds(String summonerName, List<GameIdResponseDto> gameIds);
    List<AnalysisData> findTop10ByUserHistory_IdOrderByCreatedAtDesc(Long userHistoryId);
    AnalysisData findAnalysisDataByGameIdAndUserHistory_Id(String gameId, Long userHistoryId);

}
