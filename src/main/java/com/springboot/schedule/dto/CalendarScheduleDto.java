package com.springboot.schedule.dto;

import com.springboot.schedule.entity.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarScheduleDto {
    @Schema(description = "일정이 속한 날짜", example = "2025-03-27")
    private LocalDate date;

    @Schema(description = "모임 이름", example = "강남역 스터디 모임")
    private String groupName;

    @Schema(description = "일정 이름", example = "스프링 주간 스터디")
    private String scheduleName;

    @Schema(description = "모임 대표 이미지 URL", example = "/images/groups/1/profile.png")
    private String groupImage;

    @Schema(description = "일정 장소", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "일정 시작 시간", example = "10:00")
    private LocalTime startTime;

    @Schema(description = "일정 종료 시간", example = "12:00")
    private LocalTime endTime;

    @Schema(description = "현재 참여 중인 인원 수", example = "5")
    private int memberCount;

    @Schema(description = "일정 참여 최대 인원 수", example = "10")
    private int maxMemberCount;

    @Schema(description = "일정 기간 (연속 일정일 경우에만 사용)", example = "2025-03-25 ~ 2025-03-28", nullable = true)
    private String duration;

    @Schema(description = "일정 종류 상태", example = "일정 종류 상태(단기, 정기, 연속)")
    private Schedule.ScheduleStatus scheduleStatus;
}