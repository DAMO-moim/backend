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
import io.swagger.v3.oas.annotations.Parameter;
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

    @GetMapping
    public ResponseEntity getMyPage(@Parameter(hidden = true) @AuthenticationPrincipal Member member){
        Member findmember = memberService.findVerifiedMember(member.getMemberId());
        MemberDto.MyPageResponse response = mapper.memberToMyPage(findmember);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    //내 게시글 조회
    @GetMapping("/boards")
    public ResponseEntity getMyBoards(@Parameter(hidden = true) @AuthenticationPrincipal Member member,
                                      @RequestParam(defaultValue = "ALL") String category,
                                      @Positive @RequestParam int page,
                                      @Positive @RequestParam int size) {
        Page<MyPageDto.BoardsResponse> boardPage = myPageService.getMyBoards(
                member.getMemberId(), category, page - 1, size);
        List<MyPageDto.BoardsResponse> content = boardPage.getContent();

        return ResponseEntity.ok(new MultiResponseDto<>(content, boardPage));
    }
}
