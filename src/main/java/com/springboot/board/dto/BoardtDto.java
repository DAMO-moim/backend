package com.springboot.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class BoardtDto {
    @Getter
    public static class Post {
        @Schema(description = "모임 게시글 생성", example = "충격적인 진실")
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @Schema(description = "게시글 본문", example = "그남자는... 사실")
        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;
    }

    @Getter
    public static class Patch{
        @Setter
        private long boardId;

        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {

    }
}
