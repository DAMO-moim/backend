package com.springboot.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    public static class Post{
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String content;
    }

    @Getter
    public static class Patch{
        @Setter
        @Schema(description = "댓글 ID", example = "1")
        private long commentId;

        @Schema(description = "댓글 내용", example = "본문이에용")
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        @Schema(description = "댓글 ID", example = "1")
        private long commentId;
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String content;
        @Schema(description = "댓글 작성일", example = "2025-03-21")
        private LocalDateTime createdAt;
        @Schema(description = "회원 ID", example = "1")
        private long memberId;
        @Schema(description = "회원 이름", example = "김철수")
        private String memberName;
    }
}
