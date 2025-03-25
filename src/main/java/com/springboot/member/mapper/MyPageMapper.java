package com.springboot.member.mapper;

import com.springboot.board.entity.Board;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.member.dto.MyPageDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyPageMapper {
    default MyPageDto.BoardsResponse boardToBoardsResponse(Board board) {
        return new MyPageDto.BoardsResponse(
                board.getBoardId(),
                board.getTitle(),
                truncate(board.getContent()),
                board.getGroup().getSubCategory().getCategory().getCategoryName(),
                board.getComments() != null ? board.getComments().size() : 0,
                board.getCreatedAt().toLocalDate(),
                board.getGroup() != null ? board.getGroup().getGroupName() : null,
                board.getImage()
        );
    }

    //본문의 내용이 길어지면 짜름
    default String truncate(String content) {
        return content.length() > 80 ? content.substring(0, 80) + "..." : content;
    }

    default MyPageDto.GroupsResponse groupToGroupsResponse(Group group, Member member){
        GroupMember.GroupRoles role = group.getGroupMembers().stream()
                .filter(groupMember -> groupMember.getMember().getMemberId().equals(member.getMemberId()))
                .map(GroupMember::getGroupRoles)
                .findFirst()
                .orElse(GroupMember.GroupRoles.GROUP_MEMBER);

        return new MyPageDto.GroupsResponse(
                group.getGroupId(),
                group.getGroupName(),
                group.getIntroduction(),
                group.getGroupMembers().size(),
                group.getMaxMemberCount(),
                group.getImage(),
                role.name()
        );
    }
}
