package com.springboot.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;


public class MemberCategoryDto {
    @Getter
    public static class Post{
        private long categoryId;
    }

    @Getter
    public static class Patch{
        @Size(min = 1, max = 3, message = "카테고리는 1~3개까지 선택 가능합니다.")
        private List<Long> categoryIds;
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        private long categoryId;
        private String categoryName;
        private int priority;
    }

}
