package com.samnamja.deboost.api.dto.fairy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiaryMakeRequestDto {
    private List<Content> contentList;

    @Getter
    public static class Content {
        @NotNull
        private String question;
        @NotNull
        private String answer;
    }
}
