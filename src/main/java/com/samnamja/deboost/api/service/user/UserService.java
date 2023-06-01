package com.samnamja.deboost.api.service.user;

import com.samnamja.deboost.api.dto.user.UserInfoResponseDto;
import com.samnamja.deboost.api.entity.user.User;
import com.samnamja.deboost.api.entity.user.UserRepository;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponseDto getMyInfo(UserAccount userAccount){
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. id=" + userAccount.getUserId()).build());
        return UserInfoResponseDto.builder()
                .userName(user.getName())
                .profileImageUrl(user.getPicture())
                .build();
    }
}
