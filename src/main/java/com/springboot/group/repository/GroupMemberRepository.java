package com.springboot.group.repository;

import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByGroupAndMember_MemberId(Group group, long memberId);
    boolean existsByGroupAndMember_MemberId(Group group, long memberId);
    void deleteAllByGroup(Group group);
    List<GroupMember> findAllByMember(Member member);
    long countByMember(Member member);
    List<GroupMember> findByMemberAndGroupRoles(Member member, GroupMember.GroupRoles groupRoles);
    Page<GroupMember> findByMemberAndGroupRoles(Member member, GroupMember.GroupRoles groupRoles, Pageable pageable);

    //특정 회원의 카테고리별 그룹조회
    @Query("SELECT gm FROM GroupMember gm WHERE gm.member = :member AND gm.group.subCategory.category.id = :categoryId")
    Page<GroupMember> findAllByMemberAndCategoryId(Member member, Long categoryId, Pageable pageable);

    //특정 회원의 카테고리별 그룹조회(권한포함)
    @Query("SELECT gm FROM GroupMember gm WHERE gm.member = :member AND gm.group.subCategory.category.id = :categoryId AND gm.groupRoles = :role")
    Page<GroupMember> findByMemberAndCategoryIdAndGroupRoles(Member member, Long categoryId,
                                                             GroupMember.GroupRoles role,
                                                             Pageable pageable);
}
