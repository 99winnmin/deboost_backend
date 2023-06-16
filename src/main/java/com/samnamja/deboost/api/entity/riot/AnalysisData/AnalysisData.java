package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.samnamja.deboost.api.entity.riot.UserHistory.UserHistory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "DEBOOST_ANALYSIS_DATA")
public class AnalysisData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_data_id")
    private Long id;

    @Column(name = "game_id", nullable = false)
    private String gameId;

    @Column(name = "primary_data_url")
    private String primaryDataUrl;

//    @Column(name = "secondary_data_url")
//    private String secondaryDataUrl;
//
//    @Column(name = "tertiary_data_url")
//    private String tertiaryDataUrl;

    @Column(name = "model_prediction")
    private Double modelPrediction;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    private UserHistory userHistory;

    @Builder
    public AnalysisData(String gameId, String primaryDataUrl, UserHistory userHistory) {
        this.gameId = gameId;
        this.userHistory = userHistory;
        this.primaryDataUrl = primaryDataUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public AnalysisData toInitialEntity(String gameId, String primaryDataUrl, UserHistory userHistory) {
        return AnalysisData.builder()
                .gameId(gameId)
                .userHistory(userHistory)
                .primaryDataUrl(primaryDataUrl)
                .build();
    }

//    public void updateSecondaryDataUrl(String secondaryDataUrl) {
//        this.secondaryDataUrl = secondaryDataUrl;
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    public void updateTertiaryDataUrl(String tertiaryDataUrl) {
//        this.tertiaryDataUrl = tertiaryDataUrl;
//        this.updatedAt = LocalDateTime.now();
//    }

    public void updateModelPrediction(Double modelPrediction) {
        this.modelPrediction = modelPrediction;
        this.updatedAt = LocalDateTime.now();
    }

    public PrimaryDataUrlAndId getPrimaryDataUrlAndId(AnalysisData analysisData) {
        return PrimaryDataUrlAndId.builder()
                .primaryDataId(analysisData.getId())
                .primaryDataUrl(analysisData.getPrimaryDataUrl())
                .build();
    }

    @Builder
    @Getter
    public static class PrimaryDataUrlAndId{
        private Long primaryDataId;
        private String primaryDataUrl;
    }

}
