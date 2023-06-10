package com.samnamja.deboost.api.entity.fairy.diary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {

    List<Diary> findAllByDeviceIdWithPagingOrderByCreatedAt(Long deviceId, Long cursor, Pageable pageable);

}
