package com.springboot.board.controller;

import com.springboot.board.dto.BoardDto;
import com.springboot.board.entity.Board;
import com.springboot.board.mapper.BoardMapper;
import com.springboot.board.service.BoardService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/groups/{group-id}/boards")
@Validated
public class BoardController {
    private final BoardService boardService;
    private final BoardMapper mapper;

    public BoardController(BoardService boardService, BoardMapper mapper) {
        this.boardService = boardService;
        this.mapper = mapper;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 등록 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })

    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardDto.Post boardPostDto,
                                       @PathVariable("group-id") Long groupId,
                                       @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        Board board = boardService.createBoard(mapper.boardPostDtoToBoard(boardPostDto), member.getMemberId(), groupId);

        return new ResponseEntity<>(new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 수정 완료"),
            @ApiResponse(responseCode = "400", description = "Board Not Found")
    })
    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive long boardId,
                                     @PathVariable("group-id") Long groupId,
                                     @Valid @RequestBody BoardDto.Patch boardPatchDto,
                                     @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        boardPatchDto.setBoardId(boardId);
        Board board = boardService.updateBoard(mapper.boardPatchDtoToBoard(boardPatchDto), member.getMemberId(),groupId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id") @Positive long boardId,
                                   @PathVariable("group-id") Long groupId,
                                   @Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                   //@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request, HttpServletResponse response){
        Board board = boardService.findBoard(boardId, member.getMemberId(),groupId);

        return new ResponseEntity<>(new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.OK);
    }

    @Operation(summary = "게시글 전체 조회", description = "게시글 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @GetMapping
    public ResponseEntity getBoards(@PathVariable("group-id") Long groupId,
                                    @Positive @RequestParam int page,
                                    @Positive @RequestParam int size,
                                    //@RequestParam String sort,
                                    @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        Page<Board> boardPage = boardService.findBoards(page -1, size, member.getMemberId(), groupId);
        List<Board> boards = boardPage.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>
                (mapper.boardsToBoardResponseDtos(boards),boardPage),HttpStatus.OK);
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive long boardId,
                                      @PathVariable("group-id") Long groupId,
                                      @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        boardService.deleteBoard(boardId, member.getMemberId(), groupId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}