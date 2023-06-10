package com.samnamja.deboost.api.entity.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageUrl FROM Image i WHERE i.type = :category")
    List<String> findByType(@Param("category") String category);

}
