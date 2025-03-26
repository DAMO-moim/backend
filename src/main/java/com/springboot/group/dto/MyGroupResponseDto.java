package com.springboot.group.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyGroupResponseDto {
    private String groupRole; // "LEADER" or "MEMBER"
    private List<GroupInfo> groups;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GroupInfo {
        @Schema(description = "그룹 ID", example = "1")
        private Long groupId;

        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String name;

        @Schema(description = "모임소개", example = "바둑초고수들")
        private String introduction;

        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @Schema(description = "모임 인원 수", example = "17")
        private int memberCount;

        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "카테고리 명", example = "스포츠")
        private String categoryName;
    }
}
