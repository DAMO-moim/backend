package com.springboot.board.dto;

import com.springboot.board.entity.Board;
import com.springboot.comment.dto.CommentDto;
import com.springboot.comment.entity.Comment;
import com.springboot.like.entity.Like;
import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class BoardtDto {
    @Getter
    public static class Post {
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;

        @NotNull
        private Board.VisibilityStatus visibility;
    }

    @Getter
    public static class Patch{
        @Setter
        private long boardId;

        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        private String title;

        @NotBlank(message = "내용은 최소한 1글자라도 있어야 합니다.")
        private String content;

        @NotNull
        private Board.VisibilityStatus visibility;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {
        private long boardId;
        private String title;
        private String content;
        private Board.VisibilityStatus visibility;
        private Board.BoardStatus boardStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private CommentDto.Response comment;
        private int likeCount;
        private int viewCount;

        public String getVisibility(){
            return visibility.getStatus();
        }
    }
}
