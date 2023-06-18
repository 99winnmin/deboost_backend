package com.samnamja.deboost.api.dto.openfeign.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlaskRequestDto {
    private String summonerName;
    private String bucketName;
    private List<String> keyNames;

    @Builder
    public FlaskRequestDto(String summonerName, String bucketName, List<String> keyNames) {
        this.summonerName = summonerName;
        this.bucketName = bucketName;
        this.keyNames = keyNames;
    }
}
