package com.samnamja.deboost.api.controller.fairy.diary;

import com.samnamja.deboost.api.dto.fairy.request.DiaryMakeRequestDto;
import com.samnamja.deboost.api.dto.fairy.response.ChatCompletionDto;
import com.samnamja.deboost.api.dto.fairy.response.FairyTaleCoverResponseDto;
import com.samnamja.deboost.api.dto.fairy.response.FairyTaleDetailResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.fairy.diary.FairyDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    private final FairyDiaryService fairyDiaryService;

    @PostMapping("/init/diary")
    public ResponseEntity<List<String>> getFairyContent
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody DiaryMakeRequestDto diaryMakeRequestDto
            )
    {
        ChatCompletionDto content = fairyDiaryService.getFairyContent(diaryMakeRequestDto);
        return ResponseEntity.ok().body(Arrays.asList(content.getChoices().get(0).getMessage().getContent().split("\\n\\n")));
    }

    @GetMapping("/diary/all")
    public ResponseEntity<List<FairyTaleCoverResponseDto>> getMyFairyTales
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @Nullable @RequestParam Long cursor,
                    @PageableDefault(size = 4) Pageable pageable
            )
    {
        List<FairyTaleCoverResponseDto> myFairyTaleCovers = fairyDiaryService.getMyFairyTales(userAccount, cursor, pageable);
        return ResponseEntity.ok().body(myFairyTaleCovers);
    }

    @GetMapping("/diary/{diaryId}")
    public ResponseEntity<FairyTaleDetailResponseDto> getFairyTale
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable Long diaryId
            )
    {
        FairyTaleDetailResponseDto fairyTaleDetailResponseDto = fairyDiaryService.getFairyTaleDetailInfo(userAccount, diaryId);
        return ResponseEntity.ok().body(fairyTaleDetailResponseDto);
    }

}
