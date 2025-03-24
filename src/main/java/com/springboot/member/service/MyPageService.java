package com.springboot.member.service;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.member.dto.MyPageDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MyPageMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MyPageService {
    private final MemberService memberService;
    private final BoardService boardService;
    private final MyPageMapper myPageMapper;

    public MyPageService(MemberService memberService, BoardService boardService, MyPageMapper myPageMapper) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.myPageMapper = myPageMapper;
    }

    public Page<MyPageDto.BoardsResponse> getMyBoards(long memberId, String category, int page, int size){
        Member findMember = memberService.findVerifiedMember(memberId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Board> boards;

        if(category.equalsIgnoreCase("ALL")){
            boards = boardService.findBoardsByMember(findMember, pageable);
        }else{
            boards = boardService.findBoardByMemberCategory(findMember, category, pageable);
        }

        return boards.map(myPageMapper::boardToBoardsResponse);
    }
}
