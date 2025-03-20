package com.springboot.group.dto;

import com.springboot.member.entity.Member;
import com.springboot.schedule.entity.Schedule;
import com.springboot.tag.entity.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class GroupDto {
    @Getter
    public static class Post {
        @NotBlank(message = "모임명은 공백이 아니어야 합니다.")
        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private String groupName;

        @NotBlank(message = "모임소개는 공백이 아니어야 합니다.")
        @Schema(description = "모임소개", example = "아마추어 5단 이상의 노인네 모임입니다.")
        private String introduction;

        @NotBlank(message = "최대 인원수는 필수사항입니다.")
        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;

        @NotBlank
        @Schema(description = "성별", example = "무관")
        private Member.Gender gender;

        @NotBlank
        @Schema(description = "최소년생", example = "1720년생")
        private String minBirth;

        @NotBlank
        @Schema(description = "최대년생", example = "2500년생")
        private String maxBirth;

        @NotBlank
        @Parameter(description = "서브카테고리 ID", example = "1")
        private Long subCategoryId;

        @NotBlank
        @Schema(description = "태그들 ID 목록", example = "[{\"tagId\": 1}, {\"tagId\": 2}]")
        private List<Tag> tags;
    }

    @Getter
    public static class Patch {
        @Setter
        private Long groupId;
        @Setter
        @Schema(description = "모임내용", example = "아마추어 5단 이상의 노인네 모임입니다.")
        private String introduction;
        @Setter
        @Schema(description = "모임 최대 인원 수", example = "20")
        private int maxMemberCount;
    }

    @AllArgsConstructor
    @Getter
    public static class Response {

        @Parameter(description = "카테고리 ID", example = "1")
        private Long categoryId;

        @Parameter(description = "그룹 ID", example = "1")
        private Long groupId;

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

        @Schema(description = "모임 최대 인원 수", example = "20")
        private Member.Gender gender;

        @Schema(description = "최소년생", example = "1750년")
        private String minBirth;

        @Schema(description = "최대년생", example = "2200년")
        private String maxBirth;

        @Schema(description = "모임명", example = "바둑 아마추어 5단이상 노장모임")
        private Long subCategoryId;

        @Schema(description = "모임 멤버 목록",
                example = "[{\"memberId\": 1, \"name\": \"홍길동\", \"Image\": \"https://example.com/profiles/alice.jpg\"}, " +
                        "{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}]")
        private List<Member> members;

        @Schema(description = "태그들 ID 목록", example = "[{\"tagId\": 1}, {\"tagId\": 2}]")
        private List<Tag> tags;

        @Schema(description = "모임 일정 목록",
                example = "[{\"groupScheduleId\": 1, \"startschedule\": \"2025.03.16 10:00\", \"endschedule\": \"2025.03.16 10:00\", " +
                        "\"participants\": [{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}], " +
                        "\"groupSchedulestatus\": \"종료상태\", \"address\": \"서울시 강남구 땡떙건물\", \"subaddress\": \"101동 101호\"}, " +
                        "{\"groupScheduleId\": 2, \"startschedule\": \"2025.03.14 10:00\", \"endschedule\": \"2025.03.14 10:00\", " +
                        "\"participants\": [{\"memberId\": 2, \"name\": \"김철수\", \"Image\": \"https://example.com/profiles/bob.jpg\"}, " +
                        "{\"memberId\": 3, \"name\": \"박영희\", \"Image\": \"https://example.com/profiles/eve.jpg\"}], " +
                        "\"groupSchedulestatus\": \"종료상태\", \"address\": \"이천시 중리동 cgv\", \"subaddress\": \"5층\"}]")
        private List<Schedule> schedules;
    }
}
