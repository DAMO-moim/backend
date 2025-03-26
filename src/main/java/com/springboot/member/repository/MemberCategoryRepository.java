package com.springboot.member.repository;

import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCategoryRepository extends JpaRepository<MemberCategory, Long> {
    Optional<MemberCategory> findTopByMemberOrderByPriorityAsc(Member member);
}
