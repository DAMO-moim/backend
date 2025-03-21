package com.springboot.comment.controller;

import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.service.CommentService;
import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/boards/{board-id}/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @Operation(summary = "댓글 등록", description = "댓글 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 등록 완료"),
            @ApiResponse(responseCode = "400", description = "Comment Validation failed")
    })
    @PostMapping
    public ResponseEntity postComment(@PathVariable("board-id") long boardId,
                                      @Valid @RequestBody CommentDto.Post commentPostDto,
                                      @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        Comment comment = commentService.createComment(boardId, mapper.commentPostDtoToComment(commentPostDto), member.getMemberId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료"),
            @ApiResponse(responseCode = "400", description = "Comment Validation failed")
    })
    @PatchMapping("{comment-id}")
    public ResponseEntity patchComment(@PathVariable("board-id") long boardId,
                                       @PathVariable("comment-id") long commentId,
                                       @Valid @RequestBody CommentDto.Patch commentPatchDto,
                                       @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        commentPatchDto.setCommentId(commentId);
        Comment comment = commentService.updateComment(boardId, mapper.commentPatchDtoToComment(commentPatchDto), member.getMemberId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 완료"),
            @ApiResponse(responseCode = "400", description = "Comment Validation failed")
    })
    @DeleteMapping("{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("board-id") long boardId,
                                        @PathVariable("comment-id") long commentId,
                                        @Parameter(hidden = true) @AuthenticationPrincipal Member member){
        commentService.deleteComment(commentId, boardId, member.getMemberId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
