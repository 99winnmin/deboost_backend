package com.samnamja.deboost.api.controller.fairy.diary;

import com.samnamja.deboost.api.dto.fairy.request.DiaryMakeRequestDto;
import com.samnamja.deboost.api.dto.fairy.response.ChatCompletionDto;
import com.samnamja.deboost.api.service.fairy.diary.FairyDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final FairyDiaryService fairyDiaryService;

    @PostMapping("/diary")
    public ChatCompletionDto getFairyContent(@RequestBody DiaryMakeRequestDto diaryMakeRequestDto) {
        return fairyDiaryService.getFairyContent(diaryMakeRequestDto);
    }
}
