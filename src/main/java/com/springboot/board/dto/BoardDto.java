package com.springboot.board.dto;

import com.springboot.comment.dto.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {
    @Getter
    public static class Post {
        @Schema(description = "게시글 제목", example = "제목이야")
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @Schema(description = "게시글 본문", example = "본문 이야")
        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;
    }

    @Getter
    public static class Patch{
        @Setter
        @Schema(hidden = true)
        private long boardId;

        @Schema(description = "게시글 제목", example = "수정된 제목이야")
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @Schema(description = "게시글 본문", example = "수정된 본문 이야")
        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        @Schema(description = "게시글 ID", example = "1")
        private long boardId;

        @Schema(description = "게시글 제목", example = "제목이야")
        private String title;

        @Schema(description = "게시글 본문", example = "본문 이야")
        private String content;

        @Schema(description = "이미지", example = "이미지 링크")
        private String image;

        @Schema(description = "작성자 ID", example = "1")
        private long memberId;

        @Schema(description = "작성자", example = "작성자명")
        private String memberName;

        @Schema(description = "작성자 프로필 사진", example = "작성자 프로필 사진")
        private String memberProfile;

        @Schema(description = "작성 일자", example = "2025-03-21")
        private LocalDateTime createdAt;

        private long commentCount;
    }
}
