package com.samnamja.deboost.api.dto.util;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataResponseDto<T> {
    private T data;

    @Builder
    public DataResponseDto(T data) {
        this.data = data;
    }
}
