package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupRecommend;
import com.springboot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRecommendRepository extends JpaRepository<GroupRecommend, Long> {
    Optional<GroupRecommend> findByGroupAndMember(Group group, Member member);
}