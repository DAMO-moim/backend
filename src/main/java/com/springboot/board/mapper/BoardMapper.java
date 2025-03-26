package com.springboot.board.mapper;


import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface BoardMapper {
    @Mapping(target = "group", ignore = true)  // 서비스 계층에서 설정
    @Mapping(target = "member", ignore = true) // 서비스 계층에서 설정
    @Mapping(target = "comments", ignore = true) // 댓글 리스트는 서비스 계층에서 관리
    @Mapping(target = "image", ignore = true) // 만약 image를 사용하지 않는다면 무시
    Board boardPostDtoToBoard(BoardDto.Post requestBody);
    Board boardPatchDtoToBoard(BoardDto.Patch requestBody);
    List<BoardDto.Response> boardsToBoardResponseDtos(List<Board> boards);

    //게시글 단일 조회
    default BoardDto.Response boardToBoardResponseDto(Board board){
        long commentCount = board.getComments().stream()
                .filter(comment -> comment.getCommentStatus() != Comment.CommentStatus.COMMENT_DELETE)
                .count();
        return new BoardDto.Response(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getImage(),
                board.getMember().getMemberId(),
                board.getMember().getName(),
                board.getMember().getImage(),
                board.getCreatedAt(),
                commentCount
        );
    }
}
