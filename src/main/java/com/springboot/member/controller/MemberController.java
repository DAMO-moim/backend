package com.springboot.member.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.MemberCategoryDto;
import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberCategory;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import com.springboot.utils.UriCreator;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {
    private static final String MEMBER_DEFAULT_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 등록 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PostMapping
    public ResponseEntity postMember(@RequestBody @Valid MemberDto.Post memberPostDto) {
        // Mapper를 통해 받은 Dto 데이터 Member로 변환
        Member member = mapper.memberPostToMember(memberPostDto);
        Member createdMember = memberService.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

        return ResponseEntity.created(location).build();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @RequestBody @Valid MemberDto.Patch memberPatchDto,
                                      @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember){
        memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchToMember(memberPatchDto), authenticatedmember.getMemberId());
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member member = memberService.findMember(memberId);
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size,
                                     @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember){
        Page<Member> memberPage = memberService.findMembers(page - 1, size, authenticatedmember.getMemberId());
        List<Member> members = memberPage.getContent();
        return new ResponseEntity<>
                (new MultiResponseDto<>
                        (mapper.membersToMemberResponses(members),memberPage),HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 삭제 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @DeleteMapping
    public ResponseEntity myDeleteMember(@Valid @RequestBody MemberDto.Delete memberDeleteDto,
                                         @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember) {
        Member member = mapper.memberDeleteToMember(memberDeleteDto);
        memberService.myDeleteMember(member, authenticatedmember.getMemberId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 삭제 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    //관리자가 회원 탈퇴시킬때 api
    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember){
        memberService.deleteMember(memberId, authenticatedmember);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //사용자의 카테고리 수정
    @PatchMapping("/categories")
    public ResponseEntity patchMemberCategory( @RequestBody @Valid MemberCategoryDto.Patch patchDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal Member member) {

        List<MemberCategory> memberCategories = mapper.dtoToMemberCategories(patchDto.getMemberCategories());
        memberService.updateMemberCategories(member.getMemberId(), memberCategories);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 회원의 카테고리 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청입니다.")
    })
    //사용자의 카테고리 내역 조회
    @GetMapping("/categories")
    public ResponseEntity getMemberCategory(@Parameter(hidden = true) @AuthenticationPrincipal Member member) {

        List<MemberCategory> memberCategories = memberService.findMemberCategroies(member.getMemberId());
        List<MemberCategoryDto.Response> responseList = mapper.memberCategoriesToResponseDto(memberCategories);

        return new ResponseEntity<>(new SingleResponseDto<>(responseList), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일을 찾았습니다."),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    //아이디 찾기 핸들러 메서드
    @PostMapping("/id")
    public ResponseEntity findIdGetMember(@Valid @RequestBody MemberDto.FindId findIdDto){
        Member member = memberService.findMemberEmail(mapper.findIdDtoToMember(findIdDto));
        MemberDto.FindIdResponse response = mapper.memberToFindId(member);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
