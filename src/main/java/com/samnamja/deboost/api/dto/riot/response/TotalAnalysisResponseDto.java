package com.samnamja.deboost.api.dto.riot.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TotalAnalysisResponseDto {
    private ManufactureAverageResponseDto manufactureInfo;
    private List<Double> predictionList;

    @Builder
    public TotalAnalysisResponseDto(ManufactureAverageResponseDto manufactureInfo, List<Double> predictionList) {
        this.manufactureInfo = manufactureInfo;
        this.predictionList = predictionList;
    }
}
