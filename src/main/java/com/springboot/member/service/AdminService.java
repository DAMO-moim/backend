package com.springboot.member.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminService {
    private final MemberService memberService;
    private final BoardService boardService;

    public AdminService(MemberService memberService, BoardService boardService) {
        this.memberService = memberService;
        this.boardService = boardService;
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

    //관리자 특정 회원의 댓글 조회
}
