package com.springboot.group.service;

import com.springboot.board.entity.Board;
import com.springboot.category.entity.Category;
import com.springboot.category.entity.SubCategory;
import com.springboot.category.repository.SubCategoryRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.dto.GroupDto;
import com.springboot.file.Service.StorageService;
import com.springboot.group.dto.GroupMemberResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRecommendRepository groupRecommendRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final GroupMapper groupMapper;
    private final TagRepository tagRepository;
    private final StorageService storageService;

    public GroupService(GroupRepository groupRepository,
                        MemberService memberService,
                        GroupMemberRepository groupMemberRepository,
                        GroupRecommendRepository groupRecommendRepository,
                        SubCategoryRepository subCategoryRepository,
                        GroupMapper groupMapper,
                        TagRepository tagRepository,
                        StorageService storageService) {
        this.groupRepository = groupRepository;
        this.memberService = memberService;
        this.groupMemberRepository = groupMemberRepository;
        this.groupRecommendRepository = groupRecommendRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.groupMapper = groupMapper;
        this.tagRepository = tagRepository;
        this.storageService = storageService;
    }


    // 모임 생성 서비스 로직 구현
    @Transactional
    public Group createGroup(Group group, long memberId, GroupDto.Post groupPostDto, MultipartFile image) {
        // (1) 회원이 존재하는지 검증
        Member member = memberService.findVerifiedMember(memberId);

        // 모임 가입한 갯수 검증
        validateGroupJoinLimit(member);

        // (2) 동일한 모임명이 이미 존재하는지 검증
        isGroupNameExists(group.getGroupName());

        // (3) 모임 최대, 최소 인원 수 검증
        validateMemberCount(group.getMaxMemberCount());

        // ✅ SubCategory 조회 후 설정
        SubCategory subCategory = subCategoryRepository.findById(groupPostDto.getSubCategoryId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SUBCATEGORY_NOT_FOUND));
        group.setSubCategory(subCategory); // ✅ 연관관계 설정

        Category category = subCategory.getCategory();

        // 카테고리별 생성 제한 검증
        validateGroupCreationLimitPerCategory(member, category.getCategoryId());

        //이미지는 반드시 있어야 한다.
        if (image == null || image.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.IMAGE_REQUIRED);
        }
        // (5) 모임 저장
        Group savedGroup = groupRepository.save(group);

        // 이미지 저장
        String pathWithoutExt = "groups/" + group.getGroupId() + "/profile"; // 혹은 groupId 이후 재지정
        String relativePath = storageService.store(image, pathWithoutExt);
        String imageUrl = "/images/" + relativePath;
        group.setImage(imageUrl);

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

    public Group updateGroup(Group group, long memberId, MultipartFile image) {
        // (1) 수정할 모임 조회
        Group existingGroup = groupRepository.findById(group.getGroupId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.GROUP_NOT_FOUND));

        // 이미지 저장
        String pathWithoutExt = "groups/" + existingGroup.getGroupId() + "/profile"; // 혹은 groupId 이후 재지정
        String relativePath = storageService.store(image, pathWithoutExt);
        String imageUrl = "/images/" + relativePath;
        group.setImage(imageUrl);

        // (2) 모임장 검증 (메서드 활용)
        validateGroupLeader(existingGroup, memberId);

        // (3) 모임 최대/최소 인원 수 검증 (2~100명)
        if (group.getMaxMemberCount() > 0) {
            // 현재 가입된 인원보다 작게 수정 못하도록 검증
            validateMaxMemberCountUpdate(existingGroup, group.getMaxMemberCount());
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

        // 모임 가입한 갯수 검증
        validateGroupJoinLimit(member);

        // 이미 가입한 회원인지 확인
        if (verifyGroupMember(member, group)) {
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

    //모임에 가입된 회원인지 검증
    public boolean verifyGroupMember(Member member, Group group) {
        return groupMemberRepository.existsByGroupAndMember_MemberId(group, member.getMemberId());
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

    @Transactional
    public void leaveGroup(long groupId, long memberId) {
        Group group = findVerifiedGroup(groupId);
        Member member = memberService.findVerifiedMember(memberId);

        GroupMember groupMember = groupMemberRepository.findByGroupAndMember_MemberId(group, member.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND_IN_GROUP));

        if (groupMember.getGroupRoles() == GroupMember.GroupRoles.GROUP_LEADER) {
            delegateGroupLeader(group, groupMember);
        }

        // ✅ 양방향 연관관계 제거
        group.getGroupMembers().remove(groupMember);
        member.getGroupMembers().remove(groupMember);

        groupMemberRepository.delete(groupMember); // 🔥 이제 정확히 삭제됨
    }

    @Transactional(readOnly = true)
    public List<GroupMemberResponseDto> memberListGroup(long groupId, long memberId, String keyword) {
        // (1) 모임 & 회원 검증
        Group group = findVerifiedGroup(groupId);
        memberService.findVerifiedMember(memberId);

        // (2) 그룹 멤버 스트림 가져오기
        Stream<GroupMember> stream = group.getGroupMembers().stream();

        // (3) 키워드가 있을 경우 이름 필터 (대소문자 무시)
        if (keyword != null && !keyword.trim().isEmpty()) {
            String processedKeyword = keyword.trim().toLowerCase();
            stream = stream.filter(gm ->
                    gm.getMember().getName().toLowerCase().contains(processedKeyword)
            );
        }

        // (4) 변환 후 리스트 반환
        return stream.map(gm -> GroupMemberResponseDto.builder()
                        .memberId(gm.getMember().getMemberId())
                        .name(gm.getMember().getName())
                        .image(gm.getMember().getImage()) // 이미지 필드가 있다고 가정
                        .build())
                .collect(Collectors.toList());
    }


    // 모임명이 이미 존재하는지 검증하는 메서드
    public void isGroupNameExists(String groupName) {
        // 공백 제거 (모든 공백 제거: 중간, 앞뒤 포함)
        String normalizedName = groupName.replaceAll("\\s+", "");

        if (groupRepository.existsByNormalizedGroupName(groupName))
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

    // 각 카테고리 별 모임 생성 제한(3개) 메서드
    private void validateGroupCreationLimitPerCategory(Member member, Long categoryId) {
        // 1. 해당 멤버가 카테고리를 가지고 있는지 검증
        boolean isInterestedCategory = member.getMemberCategories().stream()
                .anyMatch(mc -> mc.getCategory().getCategoryId().equals(categoryId));

        if (!isInterestedCategory) {
            throw new BusinessLogicException(ExceptionCode.NOT_INTERESTED_CATEGORY);
        }

        List<GroupMember> groupLeaders = groupMemberRepository.findByMemberAndGroupRoles(member, GroupMember.GroupRoles.GROUP_LEADER);

        long countInCategory = groupLeaders.stream()
                .map(GroupMember::getGroup)
                .map(Group::getSubCategory)
                .map(SubCategory::getCategory)
                .filter(category -> category.getCategoryId().equals(categoryId))
                .count();

        if (countInCategory >= 3) {
            throw new BusinessLogicException(ExceptionCode.EXCEED_CATEGORY_GROUP_CREATION_LIMIT);
        }
    }

    // 모임 가입 제한(10개) 메서드
    private void validateGroupJoinLimit(Member member) {
        long joinedCount = groupMemberRepository.countByMember(member);
        if (joinedCount >= 10) {
            throw new BusinessLogicException(ExceptionCode.EXCEED_GROUP_JOIN_LIMIT);
        }
    }

    // 모임장 위임 로직
    private void delegateGroupLeader(Group group, GroupMember leavingMember) {
        List<GroupMember> members = group.getGroupMembers().stream()
                .filter(m -> !m.equals(leavingMember)) // 탈퇴 대상은 제외
                .filter(m -> m.getGroupRoles() == GroupMember.GroupRoles.GROUP_MEMBER)
                .sorted(Comparator.comparing(GroupMember::getCreatedAt))
                .collect(Collectors.toList());

        if (members.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_MEMBER_TO_DELEGATE);
        }
        GroupMember newLeader = members.get(0);
        newLeader.setGroupRoles(GroupMember.GroupRoles.GROUP_LEADER);
    }

    // 현재 가입한 인원보다 작은 최대 인원으로 수정하는 거 막는 검증 메서드
    public void validateMaxMemberCountUpdate(Group group, int newMaxCount) {
        int currentMemberCount = group.getGroupMembers().size();
        if (newMaxCount < currentMemberCount) {
            throw new BusinessLogicException(ExceptionCode.INVALID_GROUP_CAPACITY_UPDATE);
        }
    }
    //사용자의 모임 리스트
    @Transactional(readOnly = true)
    public Page<Group> findGroupsByMember(Member member, Pageable pageable) {
        return groupRepository.findAllByMember(member, pageable);
    }

    @Transactional(readOnly = true)
    public Page<GroupMember> findGroupsByRole(Member member, GroupMember.GroupRoles role, Pageable pageable) {
        return groupMemberRepository.findByMemberAndGroupRoles(member, role, pageable);
    }

    //사용자의 카테고리별 모임 리스트
    @Transactional(readOnly = true)
    public Page<GroupMember> findGroupsByCategory(Member member, String categoryName, Pageable pageable) {
        return groupMemberRepository.findAllByMemberAndCategoryName(member, categoryName, pageable);
    }

    //사용자의 카테고리별 모임 리스트(모임장여부)
    @Transactional(readOnly = true)
    public Page<GroupMember> findGroupsByCategoryAndRole(Member member, String categoryName, GroupMember.GroupRoles roles, Pageable pageable){
        return groupMemberRepository.findByMemberAndCategoryNameAndGroupRoles(member,categoryName, roles, pageable);
    }
}
