package com.samnamja.deboost.api.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoResponseDto {
    private String userName;
    private String profileImageUrl;

    @Builder
    public UserInfoResponseDto(String userName, String profileImageUrl) {
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }
}
