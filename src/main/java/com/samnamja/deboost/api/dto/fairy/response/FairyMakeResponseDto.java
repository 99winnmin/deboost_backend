package com.samnamja.deboost.api.dto.fairy.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FairyMakeResponseDto {
    private Long id;
    private String title;
    private List<String> content;
    private String coverUrl;

    @Builder
    public FairyMakeResponseDto(Long id, String title, List<String> content, String coverUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.coverUrl = coverUrl;
    }
}
