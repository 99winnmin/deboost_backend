package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalysisDataRepository extends JpaRepository<AnalysisData, Long>, AnalysisDataRepositoryCustom {
    List<AnalysisData> findAnalysisDataBySummonerNameAndGameIds(String summonerName, List<GameIdResponseDto> gameIds);
    List<AnalysisData> findTop10ByUserHistory_IdOrderByCreatedAtDesc(Long userHistoryId);
    List<AnalysisData> findTop10ByUserHistory_IdAndAnalysisIdOrderByCreatedAtDesc(Long userHistoryId, Long cursor, Pageable pageable);
    AnalysisData findAnalysisDataByGameIdAndUserHistory_Id(String gameId, Long userHistoryId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE AnalysisData a SET a.modelPrediction = :score, a.updatedAt = :now WHERE a.primaryDataUrl = :jsonKey")
    void updateAnalysisDataPrediction(Double score, LocalDateTime now, String jsonKey);
}
