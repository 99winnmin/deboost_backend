package com.samnamja.deboost.api.entity.riot.AnalysisData;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.samnamja.deboost.api.dto.openfeign.response.GameIdResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.samnamja.deboost.api.entity.riot.AnalysisData.QAnalysisData.analysisData;
import static com.samnamja.deboost.api.entity.riot.UserHistory.QUserHistory.userHistory;

@RequiredArgsConstructor
@Repository
public class AnalysisDataRepositoryCustomImpl implements AnalysisDataRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    @Override
    public List<AnalysisData> findAnalysisDataBySummonerNameAndGameIds(String summonerName, List<GameIdResponseDto> gameIds) {
        List<String> gameIdList = gameIds.stream().map(GameIdResponseDto::getGameId).collect(Collectors.toList());
        return queryFactory
                .selectFrom(analysisData)
                .leftJoin(userHistory).on(analysisData.userHistory.id.eq(userHistory.id))
                .where(
                        userHistory.historyGamerName.eq(summonerName),
                        analysisData.gameId.in(gameIdList)
                ).fetch();
    }
}
