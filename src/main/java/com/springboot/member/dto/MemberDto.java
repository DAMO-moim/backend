package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
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


        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
        private String phoneNumber;

        @Size(min = 1, max = 3)
        private List<MemberCategoryDto.Post> memberCategories;
    }

    @Getter
    public static class Delete{
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    public static class FindId{
        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String name;
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        private String phoneNumber;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class FindIdResponse{
        private String email;
    }

    @Setter
    @Getter
    public static class Patch{
        @Schema(hidden = true)
        private long memberId;

        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang")
        private String password;

        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Response {
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

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class MemberOfGroupResponse {
        private long memberId;
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;
    }
}
