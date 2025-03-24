package com.springboot.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

public class MyPageDto {
    @Getter
    @AllArgsConstructor
    public static class BoardsResponse{
        private Long boardId;
        private String title;
        private String contentPreview;
        private String category;
        private int commentCount;
        private LocalDate createdAt;
        private String groupName;
        private String Image;
    }
}
