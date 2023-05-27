package com.samnamja.deboost.utils.aws;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FileDetail {
    private String id;
    private String name;
    private String format;
    private String path;
    private long bytes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static FileDetail multipartOf(MultipartFile multipartFile) {
        final String fileId = MultipartUtil.createFileId();
        final String format = MultipartUtil.getFormat(multipartFile.getContentType());
        return FileDetail.builder()
                .id(fileId)
                .name(multipartFile.getOriginalFilename())
                .format(format)
                .path(MultipartUtil.createPath(fileId, format))
                .bytes(multipartFile.getSize())
                .build();
    }
}
