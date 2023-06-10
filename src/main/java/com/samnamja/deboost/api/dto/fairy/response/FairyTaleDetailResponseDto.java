package com.samnamja.deboost.api.dto.fairy.response;

import com.samnamja.deboost.api.entity.fairy.diary.Diary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FairyTaleDetailResponseDto {
    private Long fairyTaleId;
    private String fairyTaleTitle;
    private String fairyTaleCoverUrl;

    private List<String> fairyTaleContent;

    @Builder
    public FairyTaleDetailResponseDto(Long fairyTaleId, String fairyTaleTitle, String fairyTaleCoverUrl, List<String> fairyTaleContent) {
        this.fairyTaleId = fairyTaleId;
        this.fairyTaleTitle = fairyTaleTitle;
        this.fairyTaleCoverUrl = fairyTaleCoverUrl;
        this.fairyTaleContent = fairyTaleContent;
    }

    public static FairyTaleDetailResponseDto of(Diary diary){
        return FairyTaleDetailResponseDto.builder()
                .fairyTaleId(diary.getId())
                .fairyTaleTitle(diary.getTitle())
                .fairyTaleCoverUrl(diary.getCoverUrl())
                .fairyTaleContent(Arrays.asList(diary.getContent().split("\\\\n\\\\n")))
                .build();
    }
}
