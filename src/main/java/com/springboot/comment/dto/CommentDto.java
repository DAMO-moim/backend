package com.springboot.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    public static class Post{
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String comment;
    }

    @Getter
    public static class Patch{
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String comment;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        private long commentId;
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String comment;
        private LocalDateTime createdAt;
        private long memberId;
        private String memberName;
    }
}
