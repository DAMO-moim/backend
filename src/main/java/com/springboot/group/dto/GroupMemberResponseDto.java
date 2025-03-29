package com.springboot.group.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberResponseDto {
    @Parameter(description = "사용자 ID", example = "1")
    private Long memberId;

    @Parameter(description = "사용자 명", example = "권택현")
    private String name;

    @Parameter(description = "사용자 프로필 사진", example = "/static/images/noImage.png")
    private String image;
}

