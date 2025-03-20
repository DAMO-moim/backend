package com.springboot.board.mapper;


import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    Board boardPostToBoard(BoardDto.Post requestBody);
    Board boardPatchToBoard(BoardDto.Post requestBody);
    BoardDto.Response boardToBoardResponse(Board board);
    List<BoardDto.Response> boardsToBoardResponses(List<Board> boards);
}
