package com.springboot.member.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.comment.entity.Comment;
import com.springboot.comment.service.CommentService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.service.GroupService;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {
    private final MemberService memberService;
    private final BoardService boardService;
    private final GroupService groupService;
    private final CommentService commentService;

    public AdminService(MemberService memberService, BoardService boardService, GroupService groupService, CommentService commentService) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.groupService = groupService;
        this.commentService = commentService;
    }

    //관리자 특정 회원 조회
    public Member adminFindMembers(long memberId, long adminId){
        //관리자가 아니라면 예외를 던진다.
        if(!memberService.isAdmin(adminId)){
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findVerifiedMember(memberId);

        return findMember;
    }
    //관리자 특정 회원의 게시글 조회
    public Page<Board> getMemberBoards(long memberId, long adminId, int page, int size){
        //관리자가 아니라면 예외를 던진다.
        if(!memberService.isAdmin(adminId)){
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return boardService.findBoardsByMember(findMember, pageable);
    }
    //관리자 특정 회원의 모임 조회
    public Page<Group> getMemberGroups(long memberId, long adminId, int page, int size){
        //관리자가 아니라면 예외를 던진다.
        if(!memberService.isAdmin(adminId)){
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return groupService.findGroupsByMember(findMember, pageable);
    }
    //관리자 특정 회원의 댓글 조회
    public Page<Comment> getMemberComments(long memberId, long adminId, int page, int size){
        //관리자가 아니라면 예외를 던진다.
        if(!memberService.isAdmin(adminId)){
            throw new BusinessLogicException(ExceptionCode.ACCESS_DENIED);
        }
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return commentService.findCommentsByMember(findMember, pageable);
    }
}
