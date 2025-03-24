package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByGroupName(String groupName);
    @Query("SELECT g FROM Group g JOIN g.groupMembers gm WHERE gm.member.memberId = :memberId")
    Page<Group> findAllByMemberId(@Param("memberId") long memberId, Pageable pageable);

    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.member = :member")
    Page<Group> findAllByMember(@Param("member") Member member, Pageable pageable);
}
