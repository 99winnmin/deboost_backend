package com.samnamja.deboost.api.dto.fairy.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryMakeRequestDto {
    private List<Content> contentList;

    @Getter
    public static class Content {
        private String question;
        private String answer;
    }
}
