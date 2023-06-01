package com.samnamja.deboost.api.dto.openfeign.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlaskRequestDto {
    private String bucketName;
    private String keyName;

    @Builder
    public FlaskRequestDto(String bucketName, String keyName){
        this.bucketName = bucketName;
        this.keyName = keyName;
    }
}
