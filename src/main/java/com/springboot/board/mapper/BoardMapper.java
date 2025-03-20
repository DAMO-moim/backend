package com.springboot.board.mapper;


import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(target = "group", ignore = true)  // 서비스 계층에서 설정
    @Mapping(target = "member", ignore = true) // 서비스 계층에서 설정
    @Mapping(target = "comments", ignore = true) // 댓글 리스트는 서비스 계층에서 관리
    @Mapping(target = "image", ignore = true) // 만약 image를 사용하지 않는다면 무시
    Board boardPostDtoToBoard(BoardDto.Post requestBody);
    Board boardPatchDtoToBoard(BoardDto.Patch requestBody);
    List<BoardDto.Response> boardsToBoardResponseDtos(List<Board> boards);

    default BoardDto.Response boardToBoardResponseDto(Board board){
        return new BoardDto.Response(
                board.getBoardId(),
                board.getTitle(),
                board.getContent(),
                board.getImage(),
                board.getMember().getMemberId(),
                board.getMember().getName()
        );
    }
    

}
