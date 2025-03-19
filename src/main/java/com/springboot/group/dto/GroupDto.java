package com.springboot.group.dto;

import com.springboot.member.entity.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class GroupDto {
    @Getter
    public static class Post {
        @Schema(description = "모임 이름", example = "축구를 합시다")
        private String groupName;

        @Schema(description = "모임 소개", example = "축구를 못하지만 좋아하는 사람들의 모임")
        private String introduction;

        @Schema(description = "모임 최대 인원수", example = "30")
        private int maxMemberCount;

        @Schema(description = "성별 조건", example = "MAN")
        private String gender;

        @Schema(description = "나이 조건(최소)", example = "1999")
        private String minBirth;

        @Schema(description = "나이 조건(최대)", example = "2005")
        private String maxBirth;

        @Valid
        private List<TagDto> tags;
    }

    @Getter
    public static class Patch{
        @Parameter(description = "모임 ID", example = "1")
        private long groupId;

        @Schema(description = "모임 소개", example = "축구를 잘하는 사람들의 모임")
        private String introduction;

        @Schema(description = "모임 최대 인원수", example = "50")
        private int maxMemberCount;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {

    }
}
