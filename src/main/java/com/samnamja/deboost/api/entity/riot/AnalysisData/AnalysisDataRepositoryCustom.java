package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;

import java.util.List;

public interface AnalysisDataRepositoryCustom {
    List<AnalysisData> findAnalysisDataBySummonerNameAndGameIds(String summonerName, List<GameIdResponseDto> gameIds);
}
