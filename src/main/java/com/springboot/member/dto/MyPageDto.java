package com.springboot.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

public class MyPageDto {
    @Getter
    @AllArgsConstructor
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

    @Getter
    @AllArgsConstructor
    public static class GroupsResponse{
        @Schema(description = "모임 ID", example = "1")
        private long groupId;
        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String groupName;

        @Schema(description = "모임소개", example = "바둑 아마추어 5단이상 노장모임")
        private String introduction;

        @Schema(description = "모임 인원 수", example = "17")
        private int memberCount;

        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @Schema(description = "모임 프로필 이미지 주소", example = "/image/uuid")
        private String Image;

        @Schema(description = "해당 모임의 모임장 여부", example = "Group_Leader")
        private String role;
    }
}
