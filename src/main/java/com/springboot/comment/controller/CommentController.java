package com.springboot.comment.controller;

import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.mapper.CommentMapper;
import com.springboot.comment.service.CommentService;
import com.springboot.member.entity.Member;
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

    @PostMapping
    public ResponseEntity postComment(@PathVariable("board-id") long boardId,
                                      @Valid @RequestBody CommentDto.Post commentPostDto,
                                      @AuthenticationPrincipal Member member){
        Comment comment = commentService.createComment(boardId, mapper.commentPostDtoToComment(commentPostDto), member.getMemberId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("{comment-id}")
    public ResponseEntity patchComment(@PathVariable("board-id") long boardId,
                                       @PathVariable("comment-id") long commentId,
                                       @Valid @RequestBody CommentDto.Patch commentPatchDto,
                                       @AuthenticationPrincipal Member member){
        commentPatchDto.setCommentId(commentId);
        Comment comment = commentService.updateComment(boardId, mapper.commentPatchDtoToComment(commentPatchDto), member.getMemberId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
