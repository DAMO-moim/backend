package com.springboot.board.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
   //private final GroupService groupService;

    public BoardService(BoardRepository boardRepository, MemberService memberService) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
    }

    public Board createBoard(Board board, long memberId, long groupId){
        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        //isMemberOfGroup(memberId, groupId);
        Member member = memberService.findVerifiedMember(memberId);

        board.setMember(member);
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Board board, long memberId, long groupId){
        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        //isMemberOfGroup(memberId, groupId);
        Member member = memberService.findVerifiedMember(memberId);

        //해당 게시글이 존재하는지 검증
        Board findBoard = findVerifiedBoard(board.getBoardId());

        //해당 게시글의 작성자인지 검증
        isBoardOwner(findBoard, memberId);

        //게시글 등록 상태가 아니라면 수정 불가능
        if(!findBoard.getBoardStatus().equals(Board.BoardStatus.BOARD_POST)){
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Optional.ofNullable(board.getTitle())
                .ifPresent(title -> findBoard.setTitle(title));
        Optional.ofNullable(board.getContent())
                .ifPresent(content -> findBoard.setContent(content));

        return boardRepository.save(findBoard);
    }

    @Transactional(readOnly = true)
    public Board findBoard(long boardId, long memberId, long groupId){
        //작성자 존재 여부와 해당 모임의 모임원인지 확인한다. (테스트 필요)
        //isMemberOfGroup(memberId, groupId);
        Member member = memberService.findVerifiedMember(memberId);

        return findVerifiedBoard(boardId);
    }

    @Transactional(readOnly = true)
    public Page<Board> findBoards(int page, int size, long memberId, long groupId) {


        //Page<Board> boards = PageRequest.of(page, size, sortType));
        return boardRepository.findAll(PageRequest.of(page, size,
                Sort.by("boardId").descending()));
    }

    public void deleteBoard(long boardId, long memberId, long groupId){
        //isMemberOfGroup(memberId, groupId);

        //삭제는 작성자만 가능해야 한다.
        Board board = findVerifiedBoard(boardId);
        isBoardOwner(board, memberId);

        board.setBoardStatus(Board.BoardStatus.BOARD_DELETE);
        boardRepository.save(board);
    }
    //게시글 존재 여부 확인
    public Board findVerifiedBoard(long boardId){
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(()->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return board;
    }

    //작성자가 맞는지 검증하는 메서드
    public void isBoardOwner(Board board, long memberId){
        //member service계층에 구현된 본인확인 메서드 재사용
        memberService.isAuthenticatedMember(board.getMember().getMemberId(), memberId);
    }

    //작성자 존재 여부와 작성자가 모임원인지 검증하는 메서드
//    public Board isMemberOfGroup(Member member, long groupId) {
//        //해당 그룹이 존재하는지 검증
////        Group group = GroupService.findVerifiedGroup(groupId);
////
////        //해당 그룹의 모임원이 맞는지 검증
////        Optional<GroupMember> groupMemberRole = group.getGroupMembers().stream()
////                .filter(gm -> gm.getMember().equals(member))
////                .findFirst();
////
////        if(groupMemberRole.isEmpty()) {
////            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
////        }
////
////        Board board = new Board();
////        board.setMember(member);
////        board.setGroup(group);
////
////        return board;
////    }
//    }
}