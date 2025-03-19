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

    //질문글 작성
    public Board createBoard(Board board, long memberId, long groupId){
        //해당 모임이 실제 존재하는지 검증(groupId받와야함)
        //Group group = groupService.findVerifiedGroup(groupId);
        Group group = new Group();
        //작성자(회원)이 실제 가입되어있는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);

        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        Optional<GroupMember> groupMemberRole = group.getGroupMembers().stream()
                .filter(gm -> gm.getMember().equals(member))
                .findFirst();

        if(groupMemberRole.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

        return boardRepository.save(board);
    }

    public Board updateBoard(Board board, long memberId, long groupId){
        //작성자(회원)이 실제 가입되어있는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);
        //해당 게시글이 존재하는지 검증
        Board findBoard = findVerifiedBoard(board.getBoardId());
        isBoardOwner(findBoard, memberId);

        //게시글 등록 상태가 아니라면 수정 불가능
        if(!findBoard.getBoardStatus().equals(Board.BoardStatus.BOARD_POST)){
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Optional.ofNullable(board.getTitle())
                .ifPresent(title -> findBoard.setTitle(title));
        Optional.ofNullable(board.getContent())
                .ifPresent(content -> findBoard.setContent(content));

        return boardRepository.save(board);
    }

    //특정 질문글 조회
    @Transactional(readOnly = true)
    public Board findBoard(long boardId, long memberId){
        Board board = findVerifiedBoard(boardId);

        return board;
    }

    //전체 질문글 조회
    @Transactional(readOnly = true)
    public Page<Board> findBoards(int page, int size, long memberId) {
        //작성자(회원)이 실제 가입되어있는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);

        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        Optional<GroupMember> groupMemberRole = group.getGroupMembers().stream()
                .filter(gm -> gm.getMember().equals(member))
                .findFirst();

        Page<Board> boards = PageRequest.of(page, size, sortType));

        return boards;
    }

    //게시글 존재 여부 확인
    public Board findVerifiedBoard(long boardId, long memberId){
        //작성자(회원)이 실제 가입되어있는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);

        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        Optional<GroupMember> groupMemberRole = group.getGroupMembers().stream()
                .filter(gm -> gm.getMember().equals(member))
                .findFirst();

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
}