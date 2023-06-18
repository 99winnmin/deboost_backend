package com.samnamja.deboost.api.entity.riot.UserHistory;

import com.samnamja.deboost.api.entity.riot.AnalysisData.AnalysisData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEBOOST_USER_HISTORY")
public class UserHistory {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @Column(name = "history_gamer_name", nullable = false, unique = true)
    private String historyGamerName;

    @Column(name = "is_searched")
    private Boolean isSearched;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "userHistory", cascade = CascadeType.ALL)
    private List<AnalysisData> analysisDataList;

    @Builder
    public UserHistory(String historyGamerName, Boolean isSearched) {
        this.historyGamerName = historyGamerName;
        this.isSearched = isSearched;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UserHistory toEntity(String historyGamerName, Boolean isSearched) {
        return UserHistory.builder()
                .historyGamerName(historyGamerName)
                .isSearched(isSearched)
                .build();
    }

    public void updateIsSearched() {
        this.isSearched = true;
        this.updatedAt = LocalDateTime.now();
    }
}
