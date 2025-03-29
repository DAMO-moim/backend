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
        @Schema(description = "사용자 ID", example = "1")
        private long memberId;

        @Schema(description = "사용자 이메일", example = "example@gmail.com")
        private String email;

        @Schema(description = "사용자 프로필 이미지", example = "/profile")
        private String image;

        @Schema(description = "사용자 이름", example = "홍성민")
        private String name;

        @Schema(description = "사용자 성별", example = "MALE")
        private Member.Gender gender;

        @Schema(description = "사용자 성별", example = "2001")
        private String birth;
    }

    @AllArgsConstructor
    @Getter
    public static class BoardsResponse{
        @Schema(description = "게시글 ID", example = "1")
        private long boardId;

        @Schema(description = "게시글 명", example = "자신있는 사람 봐라")
        private String title;

        @Schema(description = "내용 미리보기", example = "바둑 나보다 잘두는놈잇냐")
        private String contentPreview;

        @Schema(description = "카테고리 명", example = "게임/오락")
        private String category;

        @Schema(description = "댓글 수", example = "5")
        private int commentCount;

        @Schema(description = "작성 날짜", example = "2025-03-25")
        private LocalDate createdAt;

        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String groupName;

        @Schema(description = "게시글 이미지 주소", example = "/image/uuid")
        private String Image;
    }

    @AllArgsConstructor
    @Getter
    public static class GroupsResponse{
        @Schema(description = "모임 ID", example = "1")
        private long groupId;
        @Schema(description = "게시글 이미지 주소", example = "/image/uuid")
        private String image;
        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String groupName;
    }

    @AllArgsConstructor
    @Getter
    public static class CommentsResponse{
        @Schema(description = "댓글 ID", example = "1")
        private long commentId;
        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String groupName;
        @Schema(description = "게시글 명", example = "자신있는 사람 봐라")
        private String postTitle;
        @Schema(description = "내용 미리보기", example = "바둑 나보다 잘두는놈잇냐")
        private String content;
    }
}
