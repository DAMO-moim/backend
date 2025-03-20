package com.springboot.group.service;

import com.springboot.category.entity.SubCategory;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.repository.GroupMemberRepository;
import com.springboot.group.repository.GroupRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository, MemberService memberService, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.memberService = memberService;
        this.groupMemberRepository = groupMemberRepository;
    }


    // 모임 생성 서비스 로직 구현
    public Group createGroup(Group group, long memberId) {
        // (1) 회원이 존재하는지 검증
        Member member = memberService.findVerifiedMember(memberId);

        // (2) 동일한 모임명이 이미 존재하는지 검증
        isGroupNameExists(group.getGroupName());

        // (3) 모임 최대, 최소 인원 수 검증
        validateMemberCount(group.getMaxMemberCount());

        // (5) 모임 저장
        Group savedGroup = groupRepository.save(group);

        // (6) 모임장(`GroupMember`) 정보 저장
        GroupMember groupLeader = new GroupMember();
        groupLeader.setGroup(savedGroup);
        groupLeader.setMember(member);
        groupLeader.setGroupRoles(GroupMember.GroupRoles.GROUP_LEADER);

        return savedGroup;
    }

    public Group updateGroup(Group group, long memberId) {
        // (1) 수정할 모임 조회
        Group existingGroup = groupRepository.findById(group.getGroupId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

        // (2) 모임장 검증 (메서드 활용)
        validateGroupLeader(existingGroup, memberId);

        // (3) 모임 최대/최소 인원 수 검증 (2~100명)
        if (group.getMaxMemberCount() > 0) {
            validateMemberCount(group.getMaxMemberCount());
            existingGroup.setMaxMemberCount(group.getMaxMemberCount());
        }

        // (4) 모임 소개 수정
        if (group.getIntroduction() != null) {
            existingGroup.setIntroduction(group.getIntroduction());
        }

        // (5) 변경된 모임 정보 저장
        return groupRepository.save(existingGroup);
    }
    public Group findGroup(long groupId, long memberId) {
        // (1) 모임 존재 여부 확인
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

        // (2) 사용자가 해당 모임의 멤버인지 검증
        validateGroupMember(group, memberId);

        // (3) 모임 정보 반환
        return group;
    }

    public Page<Group> findGroups(int page, int size, long memberId) {
        // (1) 페이지 요청 객체 생성 (0-based index)
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // (2) 회원이 가입한 모임만 조회 (or 전체 공개 모임 조회 가능)
        Page<Group> groupPage = groupRepository.findAllByMemberId(memberId, pageable);

        return groupPage;
    }

    public void deleteGroup(long groupId, long memberId) {
        // (1) 삭제할 모임 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

        // (2) 요청한 사용자가 모임장인지 검증
        validateGroupLeader(group, memberId);

        // (3) 모임 삭제
        groupRepository.delete(group);
    }

    // 모임이 이미 존재하는지 검증하는 메서드
    public void isGroupNameExists(String groupName) {
        Optional<Group> group = groupRepository.findByGroupName(groupName);
        if (group.isPresent())
            throw new BusinessLogicException(ExceptionCode.GROUP_EXISTS);
    }

    public void validateMemberCount(int maxMemberCount) {
        if (maxMemberCount < 2 || maxMemberCount > 100) {
            throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_COUNT);
        }
    }

    public void validateGroupLeader(Group group, long memberId) {
        GroupMember groupMember = groupMemberRepository.findByGroupAndMemberId(group, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND_IN_GROUP));

        if (!groupMember.getGroupRoles().equals(GroupMember.GroupRoles.GROUP_LEADER)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_GROUP_LEADER);
        }
    }

    public void validateGroupMember(Group group, long memberId) {
        boolean isMember = groupMemberRepository.existsByGroupAndMemberId(group, memberId);

        if (!isMember) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_IN_GROUP);
        }
    }
}
