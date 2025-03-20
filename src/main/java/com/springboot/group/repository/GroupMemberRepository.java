package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndMemberId(Group group, long memberId);
    boolean existsByGroupAndMemberId(Group group, Long memberId);
}
