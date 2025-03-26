package com.springboot.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryDto {
    @AllArgsConstructor
    @Getter
    public static class ResponseDto{
        @Schema(description = "카테고리 ID", example = "스포츠")
        private long categoryId;
        @Schema(description = "카테고리 명", example = "스포츠")
        private String categoryName;
    }
}
