package com.samnamja.deboost.api.service.bookmark;

import com.samnamja.deboost.api.dto.bookmark.request.AddBookMarkRequestDto;
import com.samnamja.deboost.api.dto.bookmark.response.BookMarkInfoResponseDto;
import com.samnamja.deboost.api.entity.bookmark.BookMark;
import com.samnamja.deboost.api.entity.bookmark.BookMarkRepository;
import com.samnamja.deboost.api.entity.user.User;
import com.samnamja.deboost.api.entity.user.UserRepository;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.openfeign.RiotOpenFeignService;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookMarkService {
    @Value("${riot.api-key}")
    private String riotApiKey;
    private final RiotOpenFeignService riotOpenFeignService;
    private final UserRepository userRepository;
    private final BookMarkRepository bookMarkRepository;

    public List<BookMarkInfoResponseDto> getMyBookMarkInfos(UserAccount userAccount){
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. id=" + userAccount.getUserId()).build());
        return bookMarkRepository.findAllByUser_Id(user.getId()).stream()
                .map(bookMark -> BookMarkInfoResponseDto.toDto(bookMark.getId(), bookMark.getBookmarkGamerName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookMarkInfoResponseDto addBookMark(UserAccount userAccount, AddBookMarkRequestDto addBookMarkRequestDto) {
        Optional.of(riotOpenFeignService.getSummonerEncryptedId(addBookMarkRequestDto.getSummonerName(), riotApiKey))
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 소환사가 없습니다. summonerName=" + addBookMarkRequestDto.getSummonerName()).build());

        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. id=" + userAccount.getUserId()).build());

        BookMark bookMark = bookMarkRepository.save(BookMark.toEntity(addBookMarkRequestDto.getSummonerName(), user));
        return BookMarkInfoResponseDto.toDto(bookMark.getId(), bookMark.getBookmarkGamerName());
    }

    @Transactional
    public void deleteBookMark(UserAccount userAccount, Long bookMarkId) {
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. id=" + userAccount.getUserId()).build());
        bookMarkRepository.deleteBookMarkByIdAndUser_Id(bookMarkId, user.getId());
    }
}
