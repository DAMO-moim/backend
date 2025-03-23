package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndMember_MemberId(Group group, long memberId);
    boolean existsByGroupAndMember_MemberId(Group group, long memberId);
    void deleteAllByGroup(Group group);
    List<GroupMember> findAllByMember(Member member);
}
