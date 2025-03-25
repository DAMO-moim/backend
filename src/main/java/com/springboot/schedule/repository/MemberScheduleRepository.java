package com.springboot.schedule.repository;

import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberSchedule;
import com.springboot.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {
    boolean existsByScheduleAndMember(Schedule schedule, Member member);
}
