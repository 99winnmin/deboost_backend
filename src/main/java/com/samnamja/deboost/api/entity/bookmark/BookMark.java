package com.samnamja.deboost.api.entity.bookmark;

import com.samnamja.deboost.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEBOOST_BOOKMARK")
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @Column(name = "bookmark_gamer_name", nullable = false)
    private String historyGamerName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public BookMark(String historyGamerName, LocalDateTime createdAt, User user) {
        this.historyGamerName = historyGamerName;
        this.createdAt = createdAt;
        this.user = user;
    }

    public static BookMark toEntity(String historyGamerName, User user) {
        return BookMark.builder()
                .historyGamerName(historyGamerName)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
    }
}
