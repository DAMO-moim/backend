package com.springboot.comment.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.repository.CommentRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Comment updateComment(long boardId, Comment comment, long memberId){
        Member findMember = memberService.findVerifiedMember(memberId);
        Board findBoard = boardService.findVerifiedBoard(boardId);
        //해당 댓글이 실제로 존재하는지 확인이 필요
        Comment findComment = findVerifiedComment(comment.getCommentId());
        //해당 댓글의 작성자인지 검증이 필요하다.
        isCommentOwner(findComment, memberId);

        // 변경 내용 수정
        Optional.ofNullable(comment.getContent())
                .ifPresent(content -> findComment.setContent(content));

        return commentRepository.save(findComment);
    }

    public void deleteComment(long commentId, long boardId, long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        Board findBoard = boardService.findVerifiedBoard(boardId);
        //해당 댓글이 실제로 존재하는지 확인이 필요
        Comment findComment = findVerifiedComment(commentId);
        //해당 댓글의 작성자인지 검증이 필요하다.
        isCommentOwner(findComment, memberId);

        findComment.setCommentStatus(Comment.CommentStatus.COMMENT_DELETE);
        commentRepository.save(findComment);
    }
    //작성자가 맞는지 검증하는 메서드
    public void isCommentOwner(Comment comment, long memberId){
        memberService.isAuthenticatedMember(comment.getMember().getMemberId(), memberId);
    }

    public Comment findVerifiedComment(long commentId){
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }
}
