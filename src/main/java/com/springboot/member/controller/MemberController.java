package com.springboot.member.controller;

import com.springboot.member.dto.MemberDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.MemberMapper;
import com.springboot.member.service.MemberService;
import com.springboot.utils.UriCreator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@ApiOperation(value = "회원 정보 API", tags = {"Member Controller"})
@RestController
@RequestMapping("/test/members")
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
}
