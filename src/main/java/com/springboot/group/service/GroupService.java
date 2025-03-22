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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        groupMemberRepository.save(groupLeader);

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

        // 회원이 존재하는지
        memberService.findVerifiedMember(memberId);

        // (2) 사용자가 해당 모임의 멤버인지 검증
        validateGroupMember(group, memberId);

        // (3) 모임 정보 반환
        return group;
    }

    public Page<Group> findGroups(int page, int size, long memberId) {
        // (1) 페이지 요청 객체 생성 (0-based index)
        Pageable pageable = PageRequest.of(page, size, Sort.by("groupId").descending());

        // (2) 회원이 가입한 모임만 조회 (or 전체 공개 모임 조회 가능)
        Page<Group> groupPage = groupRepository.findAllByMemberId(memberId, pageable);

        return groupPage;
    }

    @Transactional
    public void deleteGroup(long groupId, long memberId) {
        // (1) 삭제할 모임 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

        // (2) 요청한 사용자가 모임장인지 검증
        validateGroupLeader(group, memberId);

        groupMemberRepository.deleteAllByGroup(group);

        // (3) 모임 삭제
        groupRepository.delete(group);
    }

    // Controller에 지금 모임 가입이 없음
    // 그거 넣어서 Service 계층에 비즈니스 로직이랑 연결
    // 가입했을 때 DB에 모임에 멤버가 가입되도록 해야하고
    // 그전에 해야될게 모임생성할때 내가 생성자니까 내가 그 모임에 자동가입되어야하고
    // 권한이 모임장으로 변경되어야함
//    public Group Group(long groupId, long memberId) {
//        // (1) 모임 존재 여부 확인
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));
//
//        // 회원이 존재하는지
//        memberService.findVerifiedMember(memberId);
//
//        // (2) 사용자가 해당 모임의 멤버인지 검증
//        validateGroupMember(group, memberId);
//
//        // (3) 모임 정보 반환
//        return group;
//    }

    @Transactional
    public void joinGroup(long groupId, long memberId) {
        // (1) 모임 존재 확인
        Group group = findVerifiedGroup(groupId);

        // (2) 회원 존재 확인
        Member member = memberService.findVerifiedMember(memberId);

        // (3) 이미 가입한 회원인지 확인
        boolean alreadyExists = groupMemberRepository.existsByGroupAndMember_MemberId(group, memberId);
        if (alreadyExists) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_JOINED_GROUP);
        }

        // 모임 최대 인원수 초과했는지 확인
        if (group.getGroupMembers().size() >= group.getMaxMemberCount()) {
            throw new BusinessLogicException(ExceptionCode.GROUP_FULL);
        }

        // (4) 모임원으로 등록
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setMember(member);
        groupMember.setGroupRoles(GroupMember.GroupRoles.GROUP_MEMBER);

        groupMemberRepository.save(groupMember);
    }

    // 모임이 이미 존재하는지 검증하는 메서드
    public void isGroupNameExists(String groupName) {
        Optional<Group> group = groupRepository.findByGroupName(groupName);
        if (group.isPresent())
            throw new BusinessLogicException(ExceptionCode.GROUP_EXISTS);
    }
    // 모임ID를 기준으로 모임 조회 후 있다면 그 모임을 가져오는 메서드
    public Group findVerifiedGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));
    }

    // 모임의 최대 인원 수가 유효한 범위(2명 이상, 100명 이하)인지 검증
    public void validateMemberCount(int maxMemberCount) {
        if (maxMemberCount < 2 || maxMemberCount > 100) {
            throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER_COUNT);
        }
    }

    // 주어진 회원이 해당 모임의 모임장(GroupLeader)인지 검증
    public void validateGroupLeader(Group group, long memberId) {
        GroupMember groupMember = groupMemberRepository.findByGroupAndMember_MemberId(group, memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND_IN_GROUP));

        if (!groupMember.getGroupRoles().equals(GroupMember.GroupRoles.GROUP_LEADER)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_GROUP_LEADER);
        }
    }

    // 주어진 모임(Group)에 해당 회원(memberId)이 속해 있는지 검증하는 메서드
    public void validateGroupMember(Group group, long memberId) {
        boolean isMember = groupMemberRepository.existsByGroupAndMember_MemberId(group, memberId);
        if (!isMember) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_IN_GROUP);
        }
    }

}
