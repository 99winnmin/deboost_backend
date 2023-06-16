package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnalysisDataRepositoryCustom {
    List<AnalysisData> findAnalysisDataBySummonerNameAndGameIds(String summonerName, List<GameIdResponseDto> gameIds);
    List<AnalysisData> findTop10ByUserHistory_IdAndAnalysisIdOrderByCreatedAtDesc(Long userHistoryId, Long cursor, Pageable pageable);
}
