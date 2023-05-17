package com.samnamja.deboost.api.dto.openfeign.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameIdResponseDto {
    private String gameId;
}
