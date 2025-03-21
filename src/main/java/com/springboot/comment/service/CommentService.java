package com.springboot.comment.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final BoardService boardService;

    public CommentService(CommentRepository commentRepository, MemberService memberService, BoardService boardService) {
        this.commentRepository = commentRepository;
        this.memberService = memberService;
        this.boardService = boardService;
    }

    public Comment createComment(long boardId, Comment comment, long memberId){
        Member findMember = memberService.findVerifiedMember(memberId);
        Board findBoard = boardService.findVerifiedBoard(boardId);
        comment.setBoard(findBoard);
        comment.setMember(findMember);

        //게시글에 댓글 추가
        findBoard.getComments().add(comment);
        return commentRepository.save(comment);
    }
}
