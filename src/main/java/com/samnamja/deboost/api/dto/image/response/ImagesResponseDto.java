package com.samnamja.deboost.api.dto.image.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ImagesResponseDto {
    private String type;
    private List<String> imagesList;

}
