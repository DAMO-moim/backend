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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Tag(name = "회원 컨트롤러", description = "회원 관련 컨트롤러")
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

    @Operation(summary = "이메일 유효성 검증", description = "이메일이 유효한지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 검증 완료"),
            @ApiResponse(responseCode = "409", description = "해당 이메일이 이미 존재합니다.")
    })
    @PostMapping("/email")
    public ResponseEntity validateEmail(@Valid @RequestBody MemberDto.Emailvalid emailDto) {
        memberService.verifyExistsEmail(emailDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "닉네임 유효성 검증", description = "닉네임이 유효한지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 검증 완료"),
            @ApiResponse(responseCode = "409", description = "해당 닉네임이 이미 존재합니다.")
    })
    @PostMapping("/name")
    public ResponseEntity validateName(@Valid @RequestBody MemberDto.Name nameDto) {
        memberService.verifyExistsName(nameDto.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "폰번호 유효성 검증", description = "폰번호이 유효한지 검증합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "폰번호 검증 완료"),
            @ApiResponse(responseCode = "409", description = "해당 폰번호이 이미 존재합니다.")
    })
    @PostMapping("/phone")
    public ResponseEntity validatePhone(@Valid @RequestBody MemberDto.Phone phoneDto) {
        memberService.verifyExistsPhoneNumber(phoneDto.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 가입", description = "회원 가입을 진행합니다.")
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

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "404", description = "INVALID_CREDENTIALS(비밀번호가 잘못되었음)")
    })
    @PatchMapping("/password")
    public ResponseEntity patchMember(@RequestBody @Valid MemberDto.PatchPassword passwordDto,
                                      @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember){

        memberService.updatePassword(passwordDto, authenticatedmember.getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "회원 수정(닉네임)", description = "회원 정보를 수정합니다(닉네임만).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 수정 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    //@PatchMapping("/{member-id}")
    @PatchMapping
    public ResponseEntity patchMember(//@PathVariable("member-id") @Positive long memberId,
                                      @RequestBody @Valid MemberDto.Patch memberPatchDto,
                                      @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember){
        //memberPatchDto.setMemberId(memberId);
        Member member = memberService.updateMember(mapper.memberPatchToMember(memberPatchDto), authenticatedmember.getMemberId());
        //return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "회원 조회", description = "회원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member member = memberService.findMember(memberId);
        return new ResponseEntity<>(new SingleResponseDto<>(mapper.memberToMemberResponse(member)), HttpStatus.OK);
    }

    @Operation(summary = "회원 목록 조회", description = "회원 전체 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
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

    @Operation(summary = "회원 탈퇴(자신)", description = "자신(회원)이 탈퇴 합니다.")
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

    @Operation(summary = "회원 탈퇴(관리자)", description = "관리자가 회원을 탈퇴 시킵니다.")
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

    @Operation(summary = "자신의 카테고리 수정", description = "자신의 카테고리 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "카테고리 수정 완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    //사용자의 카테고리 수정
    @PatchMapping("/categories")
    public ResponseEntity patchMemberCategory( @RequestBody @Valid MemberCategoryDto.Patch patchDto,
                                               @Parameter(hidden = true) @AuthenticationPrincipal Member member) {

        List<MemberCategory> memberCategories = mapper.dtoToMemberCategories(patchDto.getMemberCategories());
        memberService.updateMemberCategories(member.getMemberId(), memberCategories);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "자신의 카테고리 조회", description = "자신의 카테고리 정보를 조회합니다.")
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

    @Operation(summary = "아이디(이메일) 찾기", description = "잃어버린 이메일을 찾습니다.")
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

    @Operation(summary = "프로필 이미지 등록(수정)", description = "자신의 프로필 이미지를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 등록(수정)완료"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    //프로필 이미지 저장(수정) 메서드
    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity fileUpload(@RequestPart(required = false)MultipartFile profileImage,
                                     @Parameter(hidden = true) @AuthenticationPrincipal Member member){

        memberService.uploadImage(member, profileImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

