package com.springboot.group.mapper;

import com.springboot.group.dto.GroupDto;
import com.springboot.group.dto.GroupMemberResponseDto;
import com.springboot.group.dto.MyGroupResponseDto;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.entity.GroupTag;
import com.springboot.member.dto.MemberDto;
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
    default GroupDto.Response groupToGroupResponse(Group group) {
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
                // 멤버 리스트 변환
                .members(group.getGroupMembers().stream()
                        .map(groupMember -> MemberDto.MemberOfGroupResponse.builder()
                                .memberId(groupMember.getMember().getMemberId())
                                .name(groupMember.getMember().getName())
                                .build())
                        .collect(Collectors.toList()))

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
    default List<GroupDto.Response> groupsToGroupResponses(List<Group> groups) {
        return groups.stream()
                .map(this::groupToGroupResponse) // 단일 조회용 매핑 재사용
                .collect(Collectors.toList());
    }
    default MyGroupResponseDto.GroupInfo toMyGroupGroupInfo(Group group) {
        return MyGroupResponseDto.GroupInfo.builder()
                .groupId(group.getGroupId())
                .name(group.getGroupName())
                .introduction(group.getIntroduction())
                .maxMemberCount(group.getMaxMemberCount())
                .memberCount(group.getGroupMembers().size())
                .categoryId(group.getSubCategory().getCategory().getCategoryId())
                .categoryName(group.getSubCategory().getCategory().getCategoryName())
                .build();
    }
    default MyGroupResponseDto toMyGroupResponse(String groupRole, List<Group> groups) {
        List<MyGroupResponseDto.GroupInfo> groupInfos = groups.stream()
                .map(this::toMyGroupGroupInfo)
                .collect(Collectors.toList());

        return MyGroupResponseDto.builder()
                .groupRole(groupRole)
                .groups(groupInfos)
                .build();
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

}
