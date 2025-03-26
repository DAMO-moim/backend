package com.springboot.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.util.List;


public class MemberCategoryDto {
    @Getter
    public static class Post{
        @Schema(description = "카테고리 ID", example = "1")
        private long categoryId;
    }

    @Getter
    public static class Patch{
        @Size(min = 1, max = 3, message = "카테고리는 1~3개까지 선택 가능합니다.")
        private List<MemberCategoryUpdate> memberCategories;

        @Getter
        public static class MemberCategoryUpdate {
            @Schema(description = "카테고리 ID", example = "1")
            private long categoryId;

            @Size(min = 1, max = 3)
            @Schema(description = "우선순위", example = "1")
            private int priority;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Response{
        @Schema(description = "카테고리 ID", example = "1")
        private long categoryId;
        @Schema(description = "카테고리 명", example = "스포츠")
        private String categoryName;
        @Schema(description = "우선순위", example = "1")
        private int priority;
    }
}
