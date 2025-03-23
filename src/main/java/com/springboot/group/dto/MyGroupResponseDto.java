package com.springboot.group.dto;

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
        private Long groupId;
        private String name;
        private String introduction;
        private int maxMemberCount;
        private int memberCount;
        private Long categoryId;
        private String categoryName;
    }
}
