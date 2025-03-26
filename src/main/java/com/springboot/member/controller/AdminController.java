package com.springboot.member.controller;

import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.group.entity.Group;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.dto.MyPageDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.AdminMapper;
import com.springboot.member.service.AdminService;
import com.springboot.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "관리자 컨트롤러", description = "관리자 관련 컨트롤러")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper mapper;

    @Operation(summary = "특정 회원 상세 정보 조회(관리자)", description = "관리자페이지에서 특정 회원 정보를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "특정 회원 정보 조회완료"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    // 특정 회원 상세 정보 (관리자용)
    @GetMapping("/members/{member-id}")
    public ResponseEntity getMemberDetail(@PathVariable("member-id") long memberId,
                                           @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        Member findMember = adminService.adminFindMembers(memberId, member.getMemberId());
        AdminDto.MemberResponse response = mapper.responseDtoToMember(findMember);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.OK);
    }

    @Operation(summary = "특정 회원의 게시글 목록 조회(관리자)", description = "관리자페이지에서 특정 회원의 게시글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "특정 회원의 게시글 목록 조회완료"),
            @ApiResponse(responseCode = "404", description = "board not found"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    // 특정 회원의 게시글 목록 조회
    @GetMapping("/members/{member-id}/boards")
    public ResponseEntity getMemberBoards(@PathVariable("member-id") long memberId,
                                          @Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                          @Positive @RequestParam int page,
                                          @Positive @RequestParam int size){

        Page<Board> boardPage = adminService.getMemberBoards(
                memberId, member.getMemberId(),page - 1, size);
        List<AdminDto.BoardsResponse> content = boardPage.getContent().stream()
                .map(mapper::boardToBoardsResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MultiResponseDto<>(content, boardPage));
    }

    @Operation(summary = "특정 회원의 모임 목록 조회(관리자)", description = "관리자페이지에서 특정 회원의 모임 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "특정 회원의 모임 목록 조회완료"),
            @ApiResponse(responseCode = "404", description = "group not found"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    // 특정 회원의 모임 목록 조회
    @GetMapping("/members/{member-id}/groups")
    public ResponseEntity getMemberGroups(@PathVariable("member-id") long memberId,
                                          @Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                          @Positive @RequestParam int page,
                                          @Positive @RequestParam int size){


        Page<Group> groupPage = adminService.getMemberGroups(
                memberId, member.getMemberId(),page - 1, size);
        List<AdminDto.GroupsResponse> content = groupPage.getContent().stream()
                .map(mapper::groupToGroupsRepsonse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MultiResponseDto<>(content, groupPage));
    }

    @Operation(summary = "특정 회원의 댓글 목록 조회(관리자)", description = "관리자페이지에서 특정 회원의 댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "특정 회원의 댓글 목록 조회완료"),
            @ApiResponse(responseCode = "404", description = "group not found"),
            @ApiResponse(responseCode = "404", description = "member not found")
    })
    // 특정 회원의 댓글 목록 조회
    @GetMapping("/members/{member-id}/comments")
    public ResponseEntity getMemberComments(@PathVariable("member-id") long memberId,
                                            @Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                            @Positive @RequestParam int page,
                                            @Positive @RequestParam int size){
        Page<Comment> commentPage = adminService.getMemberComments(
                memberId, member.getMemberId(),page - 1, size);
        List<AdminDto.CommentsResponse> content = commentPage.getContent().stream()
                .map(mapper::commentToCommentsRepsonse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MultiResponseDto<>(content, commentPage));
    }
}
