package com.springboot.schedule.repository;

import com.springboot.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 카테고리 ID 기준 모임 일정 조회
    @Query("SELECT s FROM Schedule s WHERE s.group.subCategory.category.categoryId = :categoryId")
    Page<Schedule> findByCategoryId(@Param("categoryId") long categoryId, Pageable pageable);

    // 카테고리 이름 기준 모임 일정 조회
    @Query("SELECT s FROM Schedule s WHERE s.group.subCategory.category.categoryName = :categoryName")
    Page<Schedule> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
}
