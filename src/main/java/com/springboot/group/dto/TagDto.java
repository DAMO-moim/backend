package com.springboot.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class TagDto {
    @Schema(description = "태그 아이디", example = "2")
    @Positive
    private long tagId;

    @Schema(description = "태그 명", example = "활발한")
    private String tagName;
}
