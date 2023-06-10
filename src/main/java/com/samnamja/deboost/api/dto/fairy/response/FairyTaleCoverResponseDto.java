package com.samnamja.deboost.api.dto.fairy.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.samnamja.deboost.api.entity.fairy.diary.Diary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FairyTaleCoverResponseDto {
    private Long fairyTaleId;
    private String fairyTaleTitle;
    private String fairyTaleCoverUrl;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Builder
    public FairyTaleCoverResponseDto(Long fairyTaleId, String fairyTaleTitle, String fairyTaleCoverUrl, LocalDateTime createdAt) {
        this.fairyTaleId = fairyTaleId;
        this.fairyTaleTitle = fairyTaleTitle;
        this.fairyTaleCoverUrl = fairyTaleCoverUrl;
        this.createdAt = createdAt;
    }

    public static FairyTaleCoverResponseDto of(Diary diary){
        return FairyTaleCoverResponseDto.builder()
                .fairyTaleId(diary.getId())
                .fairyTaleTitle(diary.getTitle())
                .fairyTaleCoverUrl(diary.getCoverUrl())
                .createdAt(diary.getCreatedAt())
                .build();
    }
}
