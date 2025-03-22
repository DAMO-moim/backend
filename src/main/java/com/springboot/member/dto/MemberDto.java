package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

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

        @Schema(description = "사용자 성별", example = "MAN")
        private Member.Gender gender;

        @Schema(description = "사용자 출생년도", example = "1892")
        private String birth;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
        private String phoneNumber;

        @Size(min = 1, max = 3)
        private List<MemberCategoryDto.Post> memberCategories;
    }

    @Getter
    public static class Delete{
        private String email;
        private String password;
    }

    @Getter
    public static class Patch{
        @Setter
        @Schema(hidden = true)
        private long memberId;

        @Setter
        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang")
        private String password;

        @Setter
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        @Parameter(description = "사용자 ID", example = "1")
        private long memberId;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        private String image;

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
