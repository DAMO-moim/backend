package com.springboot.schedule.dto;

import com.springboot.member.dto.MemberDto;
import com.springboot.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ScheduleDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        @NotBlank
        @Schema(description = "일정 이름", example = "오케스트라 감상")
        private String scheduleName;

        @Schema(description = "일정 소개글", example = "25일 오케스트라 감상 모임 일정이 있겠습니다.")
        private String scheduleContent;

        @NotNull
        @Schema(description = "당일 일정", example = "SINGLE")
        private Schedule.ScheduleStatus scheduleStatus; // SINGLE, CONTINUOUS, RECURRING

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "시작 시간", example = "2025-04-01T10:00:00")
        private LocalDateTime startSchedule;

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "종료 시간", example = "2025-04-01T10:00:00")
        private LocalDateTime endSchedule;

        // 정기 일정일 경우만 사용
        @Schema(description = "정기모임 요일", example = "[\"MONDAY\", \"WEDNESDAY\", \"FRIDAY\"]")
        private List<DayOfWeek> daysOfWeek;

        @Schema(description = "장소", example = "경기도 광주시 경안로 106")
        private String address;

        @Schema(description = "상세주소", example = "해태그린아파트 102동 1107호")
        private String subAddress;

        @Min(1)
        @Parameter(description = "모집인원", example = "30")
        private int maxMemberCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Patch {
        @Setter
        private Long scheduleId;

        @Schema(description = "일정 이름", example = "스케줄명")
        private String scheduleName;

        @Schema(description = "일정 소개글", example = "스케줄 소개")
        private String scheduleContent;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "시작 시간", example = "2025-03-16T10:00:00")
        private LocalDateTime startSchedule;

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(description = "종료 시간", example = "2025-03-16T16:00:00")
        private LocalDateTime endSchedule;

        @Schema(description = "주소", example = "서울시 강남구 중앙학원")
        private String address;

        @Schema(description = "상세 주소", example = "101동 101호")
        private String subAddress;

        @Min(1)
        @Schema(description = "모집 인원", example = "30")
        private Integer maxMemberCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long groupScheduleId;

        private String scheduleName; // 일정 이름

        private String scheduleContent; // 일정 소개글

        private LocalDateTime startSchedule; // 시작 일자, 시간

        private LocalDateTime endSchedule; // 종료 일자, 시간

        private String address; // 주소

        private String subAddress; // 상세주소

        private int maxMemberCount; // 참여 최대 가능 인원

        private int memberCount; // 참여 인원 수
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleOfGroupResponse {
        private long scheduleId;
        private String scheduleName;
        private LocalDate startDate;
        private LocalTime startTime;

        private LocalDate endDate;
        private LocalTime endTime;
        private String address;
        private String subAddress;
        private Schedule.ScheduleStatus scheduleStatus;
        private Schedule.ScheduleState state;
        private List<MemberDto.MemberOfGroupResponse> members;
    }
}


