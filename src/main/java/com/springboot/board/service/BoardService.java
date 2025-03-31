package com.springboot.board.service;

import com.springboot.board.entity.Board;
import com.springboot.board.repository.BoardRepository;
import com.springboot.comment.entity.Comment;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.file.Service.StorageService;
import com.springboot.group.entity.Group;
import com.springboot.group.entity.GroupMember;
import com.springboot.group.service.GroupService;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final GroupService groupService;
    private final StorageService storageService;

    public BoardService(BoardRepository boardRepository, MemberService memberService, GroupService groupService, StorageService storageService) {
        this.boardRepository = boardRepository;
        this.memberService = memberService;
        this.groupService = groupService;
        this.storageService = storageService;
    }

    public Board createBoard(Board board, long memberId, long groupId, MultipartFile imageFile) {
        Member member = memberService.findVerifiedMember(memberId);
        Group group = groupService.findVerifiedGroup(groupId);
        //해당 모임의 모임원인지 확인한다. (테스트 필요)
        isMemberOfGroup(member, group);
        board.setMember(member);
        board.setGroup(group);

        // 파일을 가져왔을때 그 파일이 null이거나 빈 파일 일때 검증해야함
        if (imageFile != null && !imageFile.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String pathWithoutExt = "groups/" + group.getGroupId() + "/" + uuid;
            // 이미지가 저장되며 내부적으로 확장자를 붙임
            //String relativePath = storageService.store(imageFile, pathWithoutExt);
            // 실제 접근가능한 url -> 프론트가 이 링크 사용할 예정
            //String imageUrl = "/images/" + relativePath;

            String imageUrl = storageService.store(imageFile, pathWithoutExt);
            // 실제 db에 이미지 경로 저장
            board.setImage(imageUrl);
        } else {
            // 이미지가 없다면 그냥 없음 -> 텍스트만 나가야함
            board.setImage(null);
        }
        return boardRepository.save(board);
    }

    @Transactional
    public Board updateBoard(Board board, long memberId, long groupId, MultipartFile imageFile) {
        Member member = memberService.findVerifiedMember(memberId);
        Group group = groupService.findVerifiedGroup(groupId);

        //해당 게시글이 존재하는지 검증
        Board findBoard = findVerifiedBoard(board.getBoardId());

        //해당 모임의 모임원인지 확인
        isMemberOfGroup(member, group);

        //해당 게시글의 작성자인지 검증
        isBoardOwner(findBoard, memberId);

        //게시글 등록 상태가 아니라면 수정 불가능
        if (!findBoard.getBoardStatus().equals(Board.BoardStatus.BOARD_POST)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        Optional.ofNullable(board.getTitle())
                .ifPresent(title -> findBoard.setTitle(title));
        Optional.ofNullable(board.getContent())
                .ifPresent(content -> findBoard.setContent(content));

        if (imageFile != null && !imageFile.isEmpty()) {
            // 기존 이미지 삭제 (기본이미지 제외)
            String prevImage = findBoard.getImage();
            //만약 이미지가 있을경우(NULL 아니면) 저장소에서 이미지 삭제
            if (prevImage != null) {
                storageService.delete(prevImage.replace("/images/", ""));
            }

            // 새 이미지 저장
            String uuid = UUID.randomUUID().toString();
            String pathWithoutExt = "groups/" + group.getGroupId() + "/" + uuid;

            String imageUrl = storageService.store(imageFile, pathWithoutExt);
            findBoard.setImage(imageUrl);
        }
        return boardRepository.save(findBoard);
    }

    @Transactional(readOnly = true)
    public Board findBoard(long boardId, long memberId, long groupId) {
        //해당 모임의 모임원인지 확인한다.
        Group group = groupService.findVerifiedGroup(groupId);
        Member member = memberService.findVerifiedMember(memberId);
        Board findBoard = findVerifiedBoard(boardId);

        isMemberOfGroup(member, group);

        if (!findBoard.getBoardStatus().equals(Board.BoardStatus.BOARD_POST)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        return findBoard;
    }

    @Transactional(readOnly = true)
    public Page<Board> findBoards(int page, int size, long memberId, long groupId) {
        //게시글 정렬기능 있다면 생각
        //Page<Board> boards = PageRequest.of(page, size, sortType));
        Group group = groupService.findVerifiedGroup(groupId);
        Member member = memberService.findVerifiedMember(memberId);

        isMemberOfGroup(member, group);

        return boardRepository.findByGroupAndBoardStatusNot(group, Board.BoardStatus.BOARD_DELETE,
                PageRequest.of(page, size, Sort.by("boardId").descending()));
    }

    public void deleteBoard(long boardId, long memberId, long groupId) {
        Group group = groupService.findVerifiedGroup(groupId);
        Member member = memberService.findVerifiedMember(memberId);
        //해당 게시글이 있는지 검증
        Board board = findVerifiedBoard(boardId);

        //삭제는 작성자만 가능해야 한다.
        //작성자가 맞는지 검증
        isBoardOwner(board, memberId);
        isMemberOfGroup(member, group);

        if (!board.getBoardStatus().equals(Board.BoardStatus.BOARD_POST)) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }

        board.setBoardStatus(Board.BoardStatus.BOARD_DELETE);
        //게시글이 삭제되면 그 게시글의 댓글들도 삭제상태가 된다.
        board.getComments().forEach(comment ->
                comment.setCommentStatus(Comment.CommentStatus.COMMENT_DELETE)
        );
        boardRepository.save(board);
    }

    //게시글 존재 여부 확인
    public Board findVerifiedBoard(long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        Board board = optionalBoard.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return board;
    }

    //작성자가 맞는지 검증하는 메서드
    public void isBoardOwner(Board board, long memberId) {
        //member service계층에 구현된 본인확인 메서드 재사용
        memberService.isAuthenticatedMember(board.getMember().getMemberId(), memberId);
    }

    //사용자의 게시글 리스트(모든 카테고리)
    public Page<Board> findBoardsByMember(Member member, Pageable pageable) {
        return boardRepository.findByMemberAndBoardStatusNot(member, Board.BoardStatus.BOARD_DELETE, pageable);
    }

    //사용자의 게시글 리스트(카테고리 별)
    public Page<Board> findBoardByMemberCategory(Member member, Long categoryId, Pageable pageable){
        return boardRepository.findByMemberAndGroup_SubCategory_Category_CategoryIdAndBoardStatusNot(
                member, categoryId, Board.BoardStatus.BOARD_DELETE, pageable);
    }

    //작성자 존재 여부와 작성자가 모임원인지 검증하는 메서드
    public void isMemberOfGroup(Member member, Group group) {
        //해당 그룹의 모임원이 맞는지 검증
        Optional<GroupMember> groupMemberRole = group.getGroupMembers().stream()
                .filter(gm -> gm.getMember().equals(member))
                .findFirst();

        if (groupMemberRole.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_IN_GROUP);
        }
    }
}