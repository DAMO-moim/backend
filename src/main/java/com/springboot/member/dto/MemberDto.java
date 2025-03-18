package com.springboot.member.dto;

import com.springboot.member.entity.Member;
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
        private String email;

        @NotBlank
        private String password;

        private Member.Gender gender;

        private String birth;

        @NotBlank(message = "닉네임은 공백이 아니어야 합니다.")
        private String name;

        private String phoneNumber;
    }

    @Getter
    public static class Patch{
        @Setter
        private long memberId;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private long memberId;
        private String email;
        private String name;
        private String birth;
        private Member.Gender gender;
        private Member.MemberStatus memberStatus;

        public String getMemberStatus() {
            return memberStatus.getStatus();
        }
    }
}
