package com.samnamja.deboost.api.controller.bookmark;

import com.samnamja.deboost.api.dto.bookmark.request.AddBookMarkRequestDto;
import com.samnamja.deboost.api.dto.bookmark.response.BookMarkInfoResponseDto;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.bookmark.BookMarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookMarkController {
    private final BookMarkService bookMarkService;

    // 1. GET 내 북마크 불러오기
    @GetMapping(value = "/bookmark")
    public ResponseEntity<List<BookMarkInfoResponseDto>> getMyBookMarkInfos(@AuthenticationPrincipal UserAccount userAccount) {
        List<BookMarkInfoResponseDto> myBookMarkInfos = bookMarkService.getMyBookMarkInfos(userAccount);
        return ResponseEntity.ok().body(myBookMarkInfos);
    }

    // 2. POST 북마크 등록
    @PostMapping(value = "/bookmark")
    public ResponseEntity<BookMarkInfoResponseDto> addBookMark(@AuthenticationPrincipal UserAccount userAccount, @RequestBody AddBookMarkRequestDto addBookMarkRequestDto) {
        BookMarkInfoResponseDto bookMarkInfoResponseDto = bookMarkService.addBookMark(userAccount, addBookMarkRequestDto);
        return ResponseEntity.ok().body(bookMarkInfoResponseDto);
    }

    @DeleteMapping(value = "/bookmark/{bookMarkId}")
    public ResponseEntity<Void> deleteBookMark(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long bookMarkId) {
        bookMarkService.deleteBookMark(userAccount, bookMarkId);
        return ResponseEntity.ok().build();
    }
}
