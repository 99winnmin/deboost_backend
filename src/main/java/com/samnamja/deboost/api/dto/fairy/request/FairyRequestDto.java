package com.samnamja.deboost.api.dto.fairy.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FairyRequestDto {
    private String model;
    private List<MessageRequestDto> messages;

    @Builder
    public FairyRequestDto(String model, List<MessageRequestDto> messages) {
        this.model = model;
        this.messages = messages;
    }
}
