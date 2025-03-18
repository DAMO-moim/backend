package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {
    @Getter
    public static class Post {
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email
        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @NotBlank
        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang")
        private String password;

        @Schema(description = "사용자 성별", example = "Girl")
        private Member.Gender gender;

        @Schema(description = "사용자 출생년도", example = "1892")
        private String birth;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
        private String phoneNumber;
    }

    @Getter
    public static class Patch{
        @Setter
        @Parameter(description = "사용자 ID", example = "1")
        private long memberId;

        @Setter
        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang")
        private long password;

        @Setter
        @Schema(description = "사용자 이름", example = "홍성민")
        private long name;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        @Parameter(description = "사용자 ID", example = "1")
        private long memberId;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 출생년도", example = "1892")
        private String birth;

        @Schema(description = "사용자 성별", example = "Girl")
        private Member.Gender gender;

        @Schema(description = "사용자 상태", example = "MEMBER_ACTIVE")
        private Member.MemberStatus memberStatus;

        public String getMemberStatus() {
            return memberStatus.getStatus();
        }
    }
}
