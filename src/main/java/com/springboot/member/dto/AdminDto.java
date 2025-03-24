package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AdminDto {
    @AllArgsConstructor
    @Getter
    public static class MemberResponse{
        private long memberId;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @Schema(description = "사용자 프로필 이미지", example = "/profile")
        private String image;

        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 성별", example = "Girl")
        private Member.Gender gender;
    }
}
