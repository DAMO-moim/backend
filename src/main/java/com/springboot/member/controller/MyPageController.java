package com.springboot.member.controller;

import com.springboot.board.entity.Board;
import com.springboot.board.service.BoardService;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.group.service.GroupService;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.dto.MyPageDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.mapper.MyPageMapper;
import com.springboot.member.service.MemberService;
import com.springboot.member.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@Tag(name = "마이페이지 컨트롤러", description = "마이페이지 관련 컨트롤러")
@RestController
@RequestMapping("/mypage")
public class MyPageController {
    private final MemberService memberService;
    private final GroupService groupService;
    private final MemberMapper mapper;
    private final MyPageMapper myPageMapper;
    private final MyPageService myPageService;

    public MyPageController(MemberService memberService, GroupService groupService, MemberMapper mapper, MyPageMapper myPageMapper, MyPageService myPageService) {
        this.memberService = memberService;
        this.groupService = groupService;
        this.mapper = mapper;
        this.myPageMapper = myPageMapper;
        this.myPageService = myPageService;
    }

    //내 정보 조회
    @Operation(summary = "마이페이지(내 정보 조회)", description = "마이페이지에 필요한 내 정보만 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "마이페이지 내 정보 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping
    public ResponseEntity getMyPage(@Parameter(hidden = true) @AuthenticationPrincipal Member member){
        Member findmember = memberService.findVerifiedMember(member.getMemberId());
        MemberDto.MyPageResponse response = mapper.memberToMyPage(findmember);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    //내 게시글 조회
    @Operation(summary = "마이페이지(내 게시글 조회)", description = "마이페이지에서 내 게시글 조회를 눌렀을 경우 내 게시글 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "마이페이지 내 게시글 조회 완료"),
            @ApiResponse(responseCode = "404", description = "board not found"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    @GetMapping("/boards")
    public ResponseEntity getMyBoards(@Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                      @RequestParam(required = false) Long categoryId,
                                      @Positive @RequestParam int page,
                                      @Positive @RequestParam int size) {
        Page<MyPageDto.BoardsResponse> boardPage = myPageService.getMyBoards(
                member.getMemberId(), categoryId, page - 1, size);
        List<MyPageDto.BoardsResponse> content = boardPage.getContent();

        return ResponseEntity.ok(new MultiResponseDto<>(content, boardPage));
    }

    //내 모임 조회
    @Operation(summary = "마이페이지(내 모임 조회)", description = "마이페이지에서 내 모임 조회를 눌렀을 경우 내 모임 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "마이페이지 내 모임 조회 완료"),
            @ApiResponse(responseCode = "404", description = "board not found"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    @GetMapping("/groups")
    public ResponseEntity getMyGroups(@Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                      @RequestParam(required = false) Long categoryId,
                                      @RequestParam(defaultValue = "false") boolean leaderOnly, //true면 모임장인거만 보여야함
                                      @Positive @RequestParam int page,
                                      @Positive @RequestParam int size) {
        Page<MyPageDto.GroupsResponse> groupPage = myPageService.getMyGroups(
                member.getMemberId(), categoryId, leaderOnly, page - 1 , size);
        List<MyPageDto.GroupsResponse> content = groupPage.getContent();

        return ResponseEntity.ok(new MultiResponseDto<>(content, groupPage));
    }
}
