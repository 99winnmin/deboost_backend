package com.samnamja.deboost.api.entity.fairy.diary;

import com.samnamja.deboost.api.entity.fairy.device.Device;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FAIRY_DIARY")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(name = "diary_title", nullable = false)
    private String title;

    @Column(name = "diary_content", nullable = false)
    private String content;

    @Column(name = "diary_cover_url", nullable = false)
    private String coverUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Builder
    public Diary(String title, String content, Device device, String coverUrl) {
        this.title = title;
        this.content = content;
        this.device = device;
        this.coverUrl = coverUrl;
        this.createdAt = LocalDateTime.now();
    }

    public static Diary toEntity(String title, String content, Device device, String coverUrl) {
        return Diary.builder()
                .title(title)
                .content(content)
                .device(device)
                .coverUrl(coverUrl)
                .build();
    }
}
