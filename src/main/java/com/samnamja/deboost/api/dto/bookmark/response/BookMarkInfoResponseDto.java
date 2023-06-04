package com.samnamja.deboost.api.dto.bookmark.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMarkInfoResponseDto {
    private Long bookmarkId;
    private String bookmarkGamerName;

    @Builder
    public BookMarkInfoResponseDto(Long bookmarkId, String bookmarkGamerName) {
        this.bookmarkId = bookmarkId;
        this.bookmarkGamerName = bookmarkGamerName;
    }

    public static BookMarkInfoResponseDto toDto(Long bookmarkId, String bookmarkGamerName) {
        return BookMarkInfoResponseDto.builder()
                .bookmarkId(bookmarkId)
                .bookmarkGamerName(bookmarkGamerName)
                .build();
    }
}
