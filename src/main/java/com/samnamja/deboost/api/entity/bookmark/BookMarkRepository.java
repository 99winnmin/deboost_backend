package com.samnamja.deboost.api.entity.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    List<BookMark> findAllByUser_Id(Long userId);
    void deleteBookMarkByIdAndUser_Id(Long bookmarkId, Long userId);
}
