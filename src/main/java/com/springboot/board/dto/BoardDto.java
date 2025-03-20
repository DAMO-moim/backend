package com.springboot.board.dto;

import com.springboot.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {
    @Getter
    public static class Post {
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;

       // private String image
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
        private long boardId;
        private String title;
        private String content;
        private String image;
        private long memberId;
        private String memberName;
        private LocalDateTime createdAt;
        private List<CommentDto.Response> comments;
    }
}
