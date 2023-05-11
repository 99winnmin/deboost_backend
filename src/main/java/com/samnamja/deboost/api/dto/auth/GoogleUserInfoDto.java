package com.samnamja.deboost.api.dto.auth;

import lombok.Data;

@Data
public class GoogleUserInfoDto {
    private String email;
    private String name;
    private String picture;
}
