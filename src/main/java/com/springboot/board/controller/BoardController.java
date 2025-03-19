package com.springboot.board.controller;

import com.springboot.board.dto.BoardtDto;
import com.springboot.board.service.BoardService;
import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/groups/{group-id}/boards")
@Validated
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 등록 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    // 질문 생성
    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody BoardtDto.Post boardPostDto,
                                       @AuthenticationPrincipal Member member) {
//        Board board = boardService.createBoard(mapper.boardPostDtoToBoard(boardPostDto), member.getMemberId());
//        return new ResponseEntity<>(new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.CREATED);
        return null;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 수정 완료"),
            @ApiResponse(responseCode = "400", description = "Board Not Found")
    })
    @PatchMapping("/{board-id}")
    public ResponseEntity patchBoard(@PathVariable("board-id") @Positive long boardId,
                                     @Valid @RequestBody BoardtDto.Patch boardPatchDto,
                                     @AuthenticationPrincipal Member member){
        boardPatchDto.setBoardId(boardId);
        // Board board = boardService.updateBoard(mapper.boardPatchDtoToBoard(boardPatchDto), member.getMemberId());
        // return new ResponseEntity<>(new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.OK);
        return null;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @GetMapping("/{board-id}")
    public ResponseEntity getBoard(@PathVariable("board-id") @Positive long boardId,
                                   @AuthenticationPrincipal Member member,
                                   //@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request, HttpServletResponse response){
//        Board board = boardService.findBoard(boardId, member.getMemberId());
        // return new ResponseEntity<>(new SingleResponseDto<>(mapper.boardToBoardResponseDto(board)), HttpStatus.OK);
        return null;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size,
                                    @RequestParam String sort,
                                    @AuthenticationPrincipal Member member){
//        Page<Board> boardPage = boardService.findBoards(page -1, size, sort, member.getMemberId());
//        List<Board> boards = boardPage.getContent();

//        return new ResponseEntity<>(new MultiResponseDto<>
//                (mapper.boardsToBoardResponsesDtos(boards),boardPage),HttpStatus.OK);
        return null;
    }

    @Operation(summary = "게시글 등록", description = "게시글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "Board Validation failed")
    })
    @DeleteMapping("/{board-id}")
    public ResponseEntity deleteBoard(@PathVariable("board-id") @Positive long boardId,
                                      @AuthenticationPrincipal Member member){
//        boardService.deleteBoard(boardId, member.getMemberId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}