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
    //게시글 전체 조회(댓글x)
    default BoardDto.Responses boardToBoardResponsesDto(Board board){
        return new BoardDto.Responses(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getImage(),
                board.getMember().getMemberId(),
                board.getMember().getName(),
                board.getCreateAt()
        );
    }

    //게시글 단일 조회(댓글o)
    default BoardDto.Response boardToBoardResponseDto(Board board){
        return new BoardDto.Response(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getImage(),
                board.getMember().getMemberId(),
                board.getMember().getName(),
                board.getCreateAt(),
                //리스트 요소마다 Response를 담아서 응답으로 보낸다
                board.getComments().stream()
                        .map(comment -> new CommentDto.Response(
                                comment.getCommentId(),
                                comment.getContent(),
                                comment.getCreateAt(),
                                comment.getMember().getMemberId(),
                                comment.getMember().getName()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
