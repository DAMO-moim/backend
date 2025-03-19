package com.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "공통 응답 DTO")
public class SingleResponseDto<T> {
    @Schema(description = "실제 데이터")
    private T data;
}
