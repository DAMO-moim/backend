package com.springboot.member.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

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

    @AllArgsConstructor
    @Getter
    public static class BoardsResponse{
        private long boardId;
        private String title;
        private String contentPreview;
        private String category;
        private int commentCount;
        private LocalDate createdAt;
        private String groupName;
        private String Image;
    }

    @AllArgsConstructor
    @Getter
    public static class GroupsResponse{
        private long groupId;
        private String image;
        private String groupName;
    }

    @AllArgsConstructor
    @Getter
    public static class CommentsResponse{
        private long commentId;
        private String groupName;
        private String postTitle;
        private String content;
    }
}
