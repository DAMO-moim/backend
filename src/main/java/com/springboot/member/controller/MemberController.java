package com.springboot.member.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import com.springboot.utils.UriCreator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@ApiOperation(value = "회원 정보 API", tags = {"Member Controller"})
@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private static final String MEMBER_DEFAULT_URL = "/test/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "회원 정보 등록", notes = "회원 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원 등록 완료"),
            @ApiResponse(code = 404, message = "Member not found")
    })
    @PostMapping
    public ResponseEntity postMember(@RequestBody @Valid MemberDto.Post memberPostDto) {
        // Mapper를 통해 받은 Dto 데이터 Member로 변환
        Member member = mapper.memberPostToMember(memberPostDto);
        Member createdMember = memberService.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

        return ResponseEntity.created(location).build();
    }
    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원 수정 완료"),
            @ApiResponse(code = 404, message = "Member Not Found")
    })
    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberDto.Patch memberPatchDto,
                                      @AuthenticationPrincipal Member authenticatedmember){
        memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchToMember(memberPatchDto), authenticatedmember.getMemberId());
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
    }

    @ApiOperation(value = "회원 정보 단일 조회", notes = "회원 단일 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원 조회 완료"),
            @ApiResponse(code = 404, message = "Member Not Found")
    })
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member member = memberService.findMember(memberId);
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
    }

    @ApiOperation(value = "회원 정보 전체 조회", notes = "회원 전체 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size,
                                     @AuthenticationPrincipal Member authenticatedmember){
        Page<Member> memberPage = memberService.findMembers(page - 1, size, authenticatedmember.getMemberId());
        List<Member> members = memberPage.getContent();
        return new ResponseEntity<>
                (new MultiResponseDto<>
                        (mapper.membersToMemberResponses(members),memberPage),HttpStatus.OK);
    }

    @ApiOperation(value = "회원 정보 삭제", notes = "회원 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "회원 삭제 완료"),
            @ApiResponse(code = 404, message = "Member Not Found")
    })
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       @AuthenticationPrincipal Member authenticatedmember){
//        memberService.deleteMember(memberId, authenticatedmember.getMemberId());
//
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
