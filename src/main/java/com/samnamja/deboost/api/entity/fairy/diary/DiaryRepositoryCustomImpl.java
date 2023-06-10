package com.samnamja.deboost.api.entity.fairy.diary;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.samnamja.deboost.api.entity.fairy.diary.QDiary.diary;

@Repository
@RequiredArgsConstructor
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Diary> findAllByDeviceIdWithPagingOrderByCreatedAt(Long deviceId, Long cursor, Pageable pageable) {
        JPAQuery<Diary> query = jpaQueryFactory
                .selectFrom(diary)
                .where(
                        diary.device.id.eq(deviceId),
                        ltDiaryId(cursor)
                )
                .orderBy(diary.createdAt.desc())
                .limit(pageable.getPageSize());
        return query.fetch();
    }

    private BooleanExpression ltDiaryId(Long cursor) {
        return cursor == null ?
                null :
                diary.id.lt(cursor);
    }
}
