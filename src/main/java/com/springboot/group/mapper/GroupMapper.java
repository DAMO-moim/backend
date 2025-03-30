package com.springboot.group.mapper;

import com.springboot.group.dto.GroupDto;
import com.springboot.group.dto.GroupMemberResponseDto;
import com.springboot.group.dto.MyGroupResponseDto;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.entity.GroupTag;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.tag.dto.GroupTagResponseDto;
import com.springboot.tag.dto.TagResponseDto;
import com.springboot.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group groupPostToGroup(GroupDto.Post groupPost);
    Group groupPatchToGroup(GroupDto.Patch groupPatch);
    default GroupDto.Response groupToGroupResponse(Group group, Member currentUser) {
        //만약 모임원이 아닐경우 NON_MEMBER로 처리
        String myRole = group.getGroupMembers().stream()
                .filter(gm -> gm.getMember().getMemberId().equals(currentUser.getMemberId()))
                .map(gm -> gm.getGroupRoles().name()) // "GROUP_LEADER" or "GROUP_MEMBER"
                .findFirst()
                .orElse("NON_MEMBER");

        GroupDto.Response.ResponseBuilder builder = GroupDto.Response.builder()
                .categoryId(group.getSubCategory().getCategory().getCategoryId())
                .groupId(group.getGroupId())
                .image(group.getImage())
                .name(group.getGroupName())
                .introduction(group.getIntroduction())
                .maxMemberCount(group.getMaxMemberCount())
                .memberCount(group.getGroupMembers().size())
                .gender(group.getGender())
                .minBirth(group.getMinBirth())
                .maxBirth(group.getMaxBirth())
                .recommend(group.getRecommend())
                .subCategoryName(group.getSubCategory().getSubCategoryName())
                .myRole(myRole)
                // 멤버 리스트 변환
                .members(group.getGroupMembers().stream()
                        .map(groupMember -> MemberDto.MemberOfGroupResponse.builder()
                                .memberId(groupMember.getMember().getMemberId())
                                .image(groupMember.getMember().getImage())
                                //.name(groupMember.getMember().getName())
                                .build())
                        .collect(Collectors.toList())
                )
                .schedules(group.getSchedules().stream()
                        .map(schedule -> ScheduleDto.ScheduleOfGroupResponse.builder()
                                .scheduleId(schedule.getScheduleId())
                                .scheduleName(schedule.getScheduleName())
                                .startDate(schedule.getStartSchedule().toLocalDate())
                                .startTime(schedule.getStartSchedule().toLocalTime())
                                .endDate(schedule.getEndSchedule().toLocalDate())
                                .endTime(schedule.getEndSchedule().toLocalTime())
                                .address(schedule.getAddress())
                                .subAddress(schedule.getSubAddress())
                                .scheduleStatus(schedule.getScheduleStatus())
                                .state(schedule.getScheduleState())
                                .members(schedule.getMemberSchedules().stream()
                                        .map(memberSchedule -> MemberDto.MemberOfGroupResponse.builder()
                                                .memberId(memberSchedule.getMember().getMemberId())
                                                .image(memberSchedule.getMember().getImage())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList())
                )
                // ✅ 태그 리스트 변환
                .tags(
                        group.getGroupTags().stream()
                                .map(GroupTag::getTag)
                                .collect(Collectors.groupingBy(
                                        Tag::getTagType,
                                        Collectors.mapping(Tag::getTagName, Collectors.toList())
                                ))
                );
                return builder.build();
    }
    default List<GroupDto.CategoryResponse> groupsToGroupResponses(List<Group> groups) {
        return groups.stream()
                .map(this::groupToCategoryToGroupResponse) // 단일 조회용 매핑 재사용
                .collect(Collectors.toList());
    }

    default GroupMemberResponseDto groupMemberToResponse(GroupMember groupMember) {
        return GroupMemberResponseDto.builder()
                .memberId(groupMember.getMember().getMemberId())
                .name(groupMember.getMember().getName())
                .image(groupMember.getMember().getImage())  // image 필드 있는 경우
                .build();
    }

    default List<GroupMemberResponseDto> groupMembersToResponses(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(this::groupMemberToResponse)
                .collect(Collectors.toList());
    }

    default GroupDto.CategoryResponse groupToCategoryToGroupResponse(Group group) {
        GroupDto.CategoryResponse.CategoryResponseBuilder builder = GroupDto.CategoryResponse.builder()
                .categoryId(group.getSubCategory().getCategory().getCategoryId())
                .groupId(group.getGroupId())
                .image(group.getImage())
                .name(group.getGroupName())
                .introduction(group.getIntroduction())
                .maxMemberCount(group.getMaxMemberCount())
                .memberCount(group.getGroupMembers().size())
                .recommend(group.getRecommend())
                .subCategoryName(group.getSubCategory().getSubCategoryName())
                .tags(
                        group.getGroupTags().stream()
                                .map(GroupTag::getTag)
                                .collect(Collectors.groupingBy(
                                        Tag::getTagType,
                                        Collectors.mapping(Tag::getTagName, Collectors.toList())
                                ))
                );
        return builder.build();
    }

}
