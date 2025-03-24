package com.springboot.member.mapper;

import com.springboot.board.entity.Board;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminMapper {
    AdminDto.MemberResponse responseDtoToMember(Member member);

    default AdminDto.BoardsResponse boardToBoardsResponse(Board board) {
        return new AdminDto.BoardsResponse(
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
}
