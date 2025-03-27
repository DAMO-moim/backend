package com.springboot.schedule.repository;

import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberSchedule;
import com.springboot.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    boolean existsByScheduleAndMember(Schedule schedule, Member member);
    Optional<MemberSchedule> findByMemberAndSchedule(Member member, Schedule schedule);

    @Query("SELECT s FROM MemberSchedule ms JOIN ms.schedule s WHERE ms.member = :member AND s.group.subCategory.category.id = :categoryId ORDER BY s.startSchedule ASC")
    Page<Schedule> findSchedulesByCategoryId(
            @Param("member") Member member, @Param("categoryId") Long categoryId, Pageable pageable);
}
