package com.samnamja.deboost.api.entity.fairy.diary;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiaryRepositoryCustom {
    List<Diary> findAllByDeviceIdWithPagingOrderByCreatedAt(Long deviceId, Long cursor, Pageable pageable);
}
