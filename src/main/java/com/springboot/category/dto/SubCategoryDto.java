package com.springboot.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class SubCategoryDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private Long subCategoryId;
        private String subCategoryName;
    }
}
