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
    public static class Emailvalid{
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email(message = "이메일 형식을 잘못 입력했습니다.")
        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;
    }

    @Getter
    public static class Name{
        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?!\\s)(?!.*\\s{2,})(?!.*[~!@#$%^&*()_+=|<>?:{}\\[\\]\"';,.\\\\/`])[^\\s]{1,8}(?<!\\s)$",
                message = "닉네임은 공백 없이 8자 이내, 특수문자를 포함하지 않아야 합니다.")
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;
    }

    @Getter
    public static class Phone{
        @Pattern(regexp = "^010-\\d{3,4}-\\d{4}$",
                message = "휴대폰 번호는 010으로 시작하는 11자리 숫자와 '-'로 구성되어야 합니다.")
        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
        private String phoneNumber;
    }


    @Getter
    public static class Post {
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email(message = "이메일 형식을 잘못 입력했습니다.")
        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?=(?:.*[A-Za-z]){6,})(?=.*\\d)(?=(?:[^%$#@!]*[%$#@!]){2,})[A-Za-z\\d%$#@!]{8,20}$",
                message = "비밀번호는 8~20자 영문(최소 6자), 숫자, 특수문자(%,$,#,@,!) 2자 이상을 조합해야 합니다.")
        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang123!@")
        private String password;

        @Schema(description = "사용자 성별", example = "MALE")
        private Member.Gender gender;

        @Schema(description = "사용자 출생년도", example = "1892")
        private String birth;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?!\\s)(?!.*\\s{2,})(?!.*[~!@#$%^&*()_+=|<>?:{}\\[\\]\"';,.\\\\/`])[^\\s]{1,8}(?<!\\s)$",
                message = "닉네임은 공백 없이 8자 이내, 특수문자를 포함하지 않아야 합니다.")
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
        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        @NotBlank(message = "이메일은 공백이 아니어야 합니다.")
        @Email
        private String email;

        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang123!@")
        @Pattern(regexp = "^(?=(?:.*[A-Za-z]){6,})(?=.*\\d)(?=(?:[^%$#@!]*[%$#@!]){2,})[A-Za-z\\d%$#@!]{8,20}$",
                message = "비밀번호는 8~20자 영문(최소 6자), 숫자, 특수문자(%,$,#,@,!) 2자 이상을 조합해야 합니다.")
        @NotBlank
        private String password;
    }

    @Getter
    public static class FindId{
        @Schema(description = "사용자 이름", example = "홍성민")
        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String name;

        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
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
        @Schema(description = "이메일", example = "email1@google.com")
        private String email;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class MyPageResponse{
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @Schema(description = "사용자 전화번호", example = "010-1111-2222")
        private String phoneNumber;

        @Schema(description = "사용자 프로필 이미지", example = "/profile")
        private String image;
    }

    @Setter
    @Getter
    public static class Patch{
//        @Schema(hidden = true)
//        private long memberId;
        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?!\\s)(?!.*\\s{2,})(?!.*[~!@#$%^&*()_+=|<>?:{}\\[\\]\"';,.\\\\/`])[^\\s]{1,8}(?<!\\s)$",
                message = "닉네임은 공백 없이 8자 이내, 특수문자를 포함하지 않아야 합니다.")
        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;
    }

    @Setter
    @Getter
    public static class PatchPassword{
        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?=(?:.*[A-Za-z]){6,})(?=.*\\d)(?=(?:[^%$#@!]*[%$#@!]){2,})[A-Za-z\\d%$#@!]{8,20}$",
                message = "비밀번호는 8~20자 영문(최소 6자), 숫자, 특수문자(%,$,#,@,!) 2자 이상을 조합해야 합니다.")
        @Schema(description = "사용자 비밀번호", example = "zizonhuzzang123!@")
        private String currentPassword;

        @NotBlank(message = "비밀번호는 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?=(?:.*[A-Za-z]){6,})(?=.*\\d)(?=(?:[^%$#@!]*[%$#@!]){2,})[A-Za-z\\d%$#@!]{8,20}$",
                message = "비밀번호는 8~20자 영문(최소 6자), 숫자, 특수문자(%,$,#,@,!) 2자 이상을 조합해야 합니다.")
        @Schema(description = "사용자 비밀번호", example = "zizohnsungmean1!@")
        private String newPassword;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Response {
        @Schema(description = "사용자 ID", example = "1")
        private long memberId;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @Schema(description = "사용자 프로필 이미지", example = "/profile")
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
        @Schema(description = "사용자 ID", example = "1")
        private long memberId;
        @Schema(description = "사용자 프로필 이미지", example = "/profile")
        private String image;
//        @Schema(description = "사용자 이름", example = "홍성민")
//        private String name;
    }
}
