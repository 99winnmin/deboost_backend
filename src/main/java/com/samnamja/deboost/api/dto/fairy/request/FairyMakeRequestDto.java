package com.samnamja.deboost.api.dto.fairy.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FairyMakeRequestDto {
    private String title;
    private List<String> content;
    private String coverUrl;
}
