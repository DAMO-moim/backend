package com.springboot.group.dto;

import com.springboot.tag.dto.TagResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyGroupDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String groupRole; // "모임장" or "모임원"
        private List<GroupInfo> groups;

        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class GroupInfo {
            private Long groupId;
            private String name;
            private String introduction;
            private int maxMemberCount;
            private int memberCount;
        }
    }
}

