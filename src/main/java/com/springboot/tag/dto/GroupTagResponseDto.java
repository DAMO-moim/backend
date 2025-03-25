package com.springboot.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupTagResponseDto {
    @Schema(description = "태그 ID", example = "4")
    private Long tagId;
    @Schema(description = "태그 명", example = "축구")
    private String tagName;
}
