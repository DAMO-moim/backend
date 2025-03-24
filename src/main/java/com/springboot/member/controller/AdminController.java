package com.springboot.member.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.dto.AdminDto;
import com.springboot.member.entity.Member;
import com.springboot.member.mapper.AdminMapper;
import com.springboot.member.service.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MemberService memberService;
    private final AdminMapper mapper;

    // 회원 상세 정보 (관리자용)
    @GetMapping("/members/{member-id}")
    public ResponseEntity getMemberDetails(@PathVariable("member-id") Long memberId,
                                           @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        Member findMember = memberService.adminFindMembers(memberId, member.getMemberId());
        AdminDto.MemberResponse response = mapper.responseDtoToMember(findMember);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.OK);
    }
}
