package com.springboot.group.dto;

import com.springboot.group.entity.Group;
import com.springboot.member.dto.MemberDto;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.tag.dto.TagNameDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

public class GroupDto {
    @Getter
    public static class Post {
        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        @NotBlank(message = "모임명은 공백이 아니어야 합니다.")
        @Pattern(regexp = "^(?!\\s).*?(?<!\\s)$", message = "모임명은 앞뒤 공백이 없어야 합니다.")
        @Size(min = 1, max = 20, message = "모임명은 1자 이상 20자 이내여야 합니다.")
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9 ]+$",
                message = "모임명에는 특수문자를 사용할 수 없습니다."
        )
        private String groupName;

        @Schema(description = "모임소개", example = "아마추어 5단 이상의 노인네 모임입니다.")
        @NotBlank(message = "모임소개는 공백이 아니어야 합니다.")
        @Size(min = 10, max = 100, message = "모임 소개는 10자 이상 100자 이내여야 합니다.")
        private String introduction;

        @Min(value = 2, message = "모임 인원은 최소 2명 이상이어야 합니다.")
        @Max(value = 100, message = "모임 인원은 최대 100명까지만 가능합니다.")
        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @Schema(description = "성별", example = "NONE")
        private Group.GroupGender gender;

        @Schema(description = "최소년생", example = "1720")
        private String minBirth;

        @Schema(description = "최대년생", example = "2500")
        private String maxBirth;

        @Schema(description = "서브카테고리 ID", example = "1")
        private long subCategoryId;

        @Schema(description = "태그들 이름 목록", example = "[{\"tagName\": \"INFP\"}, {\"tagName\": \"차분한\"}]")
        private List<TagNameDto> tags;
    }

    @Getter
    @Setter
    public static class Patch {
        @Schema(hidden = true)
        private Long groupId;

        @Schema(description = "모임소개", example = "아마추어 5단 이상의 노인네 모임입니다.")
        @NotBlank(message = "모임소개는 공백이 아니어야 합니다.")
        @Size(min = 10, max = 100, message = "모임 소개는 10자 이상 100자 이내여야 합니다.")
        private String introduction;

        @Min(value = 2, message = "모임 인원은 최소 2명 이상이어야 합니다.")
        @Max(value = 100, message = "모임 인원은 최대 100명까지만 가능합니다.")
        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Setter
    @NoArgsConstructor
    public static class Response {

        @Parameter(description = "카테고리 ID", example = "1")
        @Null
        private Long categoryId;

        @Parameter(description = "그룹 ID", example = "1")
        private Long groupId;

        @Schema(description = "모임 프로필 이미지", example = "/image/profile")
        private String image;

        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String name;

        @Schema(description = "모임소개", example = "바둑 아마추어 5단이상 노장모임")
        private String introduction;

        @Schema(description = "모임 추천 수", example = "15")
        private int recommend;

        @Schema(description = "모임 인원 수", example = "17")
        private int memberCount;

        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @Schema(description = "성별조건", example = "MAN")
        private Group.GroupGender gender;

        @Schema(description = "최소년생 조건", example = "1990")
        private String minBirth;

        @Schema(description = "최대년생 조건", example = "2010")
        private String maxBirth;

        @Schema(description = "서브 카테고리 이름", example = "바둑")
        private String subCategoryName;

        private String myRole;

        @Schema(description = "모임 멤버 목록",
                example = "[{\"memberId\": 1, \"name\": \"홍길동\", \"Image\": \"https://example.com/profiles/alice.jpg\"}, " +
                        "{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}]")
        private List<MemberDto.MemberOfGroupResponse> members;

        @Schema(description = "태그들 ID 목록", example = "[{\"tagId\": 1}, {\"tagId\": 2}]")
        private Map<String, List<String>> tags;

        @Schema(description = "모임 일정 목록",
                example = "[{\"groupScheduleId\": 1, \"startschedule\": \"2025.03.16 10:00\", \"endschedule\": \"2025.03.16 10:00\", " +
                        "\"participants\": [{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}], " +
                        "\"groupSchedulestatus\": \"종료상태\", \"address\": \"서울시 강남구 땡떙건물\", \"subaddress\": \"101동 101호\"}, " +
                        "{\"groupScheduleId\": 2, \"startschedule\": \"2025.03.14 10:00\", \"endschedule\": \"2025.03.14 10:00\", " +
                        "\"participants\": [{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}, " +
                        "{\"memberId\": 3, \"name\": \"박영희\", \"Image\": \"https://example.com/profiles/eve.jpg\"}], " +
                        "\"groupSchedulestatus\": \"종료상태\", \"address\": \"이천시 중리동 cgv\", \"subaddress\": \"5층\"}]")
        private List<ScheduleDto.ScheduleOfGroupResponse> schedules;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Setter
    @NoArgsConstructor
    public static class CategoryResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Schema(description = "그룹 ID", example = "1")
        private Long groupId;

        @Schema(description = "모임 프로필 이미지", example = "/image/profile")
        private String image;

        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String name;

        @Schema(description = "모임소개", example = "바둑 아마추어 5단이상 노장모임")
        private String introduction;

        @Schema(description = "모임 추천 수", example = "15")
        private int recommend;

        @Schema(description = "모임 인원 수", example = "17")
        private int memberCount;

        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @Schema(description = "서브 카테고리 이름", example = "바둑")
        private String subCategoryName;

        @Schema(description = "태그들 ID 목록", example = "[{\"tagId\": 1}, {\"tagId\": 2}]")
        private Map<String, List<String>> tags;
    }
}
