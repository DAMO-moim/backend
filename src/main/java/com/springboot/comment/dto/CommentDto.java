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
        private long commentId;

        @Schema(description = "댓글 내용", example = "본문이에용")
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        private long commentId;
        @Schema(description = "댓글 내용", example = "본문이에용")
        private String content;
        private LocalDateTime createdAt;
        private long memberId;
        private String memberName;
    }
}
