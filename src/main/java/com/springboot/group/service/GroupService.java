package com.springboot.group.service;

import com.springboot.category.entity.SubCategory;
import com.springboot.category.repository.SubCategoryRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.dto.GroupDto;
import com.springboot.group.dto.MyGroupResponseDto;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.entity.GroupRecommend;
import com.springboot.group.entity.GroupTag;
import com.springboot.group.mapper.GroupMapper;
import com.springboot.group.repository.GroupMemberRepository;
import com.springboot.group.repository.GroupRecommendRepository;
import com.springboot.group.repository.GroupRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.tag.dto.TagNameDto;
import com.springboot.tag.entity.Tag;
import com.springboot.tag.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRecommendRepository groupRecommendRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final GroupMapper groupMapper;
    private final TagRepository tagRepository;

    public GroupService(GroupRepository groupRepository,
                        MemberService memberService,
                        GroupMemberRepository groupMemberRepository,
                        GroupRecommendRepository groupRecommendRepository,
                        SubCategoryRepository subCategoryRepository,
                        GroupMapper groupMapper,
                        TagRepository tagRepository) {
        this.groupRepository = groupRepository;
        this.memberService = memberService;
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecommendRepository = groupRecommendRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.groupMapper = groupMapper;
        this.tagRepository = tagRepository;
    }


    // 모임 생성 서비스 로직 구현
    @Transactional
    public Group createGroup(Group group, long memberId, GroupDto.Post groupPostDto) {
        // (1) 회원이 존재하는지 검증
        Member member = memberService.findVerifiedMember(memberId);

        // (2) 동일한 모임명이 이미 존재하는지 검증
        isGroupNameExists(group.getGroupName());

        // (3) 모임 최대, 최소 인원 수 검증
        validateMemberCount(group.getMaxMemberCount());

        // ✅ SubCategory 조회 후 설정
        SubCategory subCategory = subCategoryRepository.findById(groupPostDto.getSubCategoryId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SUBCATEGORY_NOT_FOUND));
        group.setSubCategory(subCategory); // ✅ 연관관계 설정

        // (5) 모임 저장
        Group savedGroup = groupRepository.save(group);

        // (6) 모임장(`GroupMember`) 정보 저장
        GroupMember groupLeader = new GroupMember();
        groupLeader.setGroup(savedGroup);
        groupLeader.setMember(member);
        groupLeader.setGroupRoles(GroupMember.GroupRoles.GROUP_LEADER);
        groupMemberRepository.save(groupLeader);

        // ✅ 태그 연결
        // ✅ (7) 태그 연결 (여기서 tagName 추출 + 등록)
        List<String> tagNames = groupPostDto.getTags().stream()
                .map(TagNameDto::getTagName)
                .collect(Collectors.toList());

        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND));

            GroupTag groupTag = new GroupTag();
            groupTag.setGroup(savedGroup);
            groupTag.setTag(tag);
            savedGroup.setGroupTag(groupTag); // 양방향 연관관계
        }

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

    @Transactional
    public void toggleRecommend(Long groupId, Long memberId) {
        Group group = findVerifiedGroup(groupId);
        Member member = memberService.findVerifiedMember(memberId);

        // 모임에 속한 멤버만 추천 가능
        validateGroupMember(group, memberId);

        Optional<GroupRecommend> optionalRecommend = groupRecommendRepository.findByGroupAndMember(group, member);

        if (optionalRecommend.isPresent()) {
            // 이미 추천한 상태 → 취소
            groupRecommendRepository.delete(optionalRecommend.get());
            group.setRecommend(group.getRecommend() - 1);
        } else {
            // 추천 추가
            GroupRecommend recommend = GroupRecommend.builder()
                    .group(group)
                    .member(member)
                    .build();
            groupRecommendRepository.save(recommend);
            group.setRecommend(group.getRecommend() + 1);
        }

        groupRepository.save(group);
    }

    public List<MyGroupResponseDto> getMyGroups(long memberId) {
        Member member = memberService.findVerifiedMember(memberId);

        List<GroupMember> groupMemberships = groupMemberRepository.findAllByMember(member);

        // 모임장/모임원으로 나눠서 그룹화
        Map<GroupMember.GroupRoles, List<Group>> grouped = groupMemberships.stream()
                .collect(Collectors.groupingBy(
                        GroupMember::getGroupRoles,
                        Collectors.mapping(GroupMember::getGroup, Collectors.toList())
                ));

        List<MyGroupResponseDto> result = new ArrayList<>();
        grouped.forEach((role, groupList) -> {
            String roleName = role == GroupMember.GroupRoles.GROUP_LEADER ? "LEADER" : "MEMBER";
            result.add(groupMapper.toMyGroupResponse(roleName, groupList));
        });

        return result;
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
