package com.springboot.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CategoryDto {
    @AllArgsConstructor
    @Getter
    public static class ResponseDto{
        private long categoryId;
        private String categoryName;
    }
}
