package com.springboot.schedule.mapper;

import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule schedulePostToSchedule(ScheduleDto.Post schedulePost);
    Schedule schedulePatchToSchedule(ScheduleDto.Patch schedulePatch);
    default ScheduleDto.ResponseBasic toBasicResponse(Schedule schedule) {
        return ScheduleDto.ResponseBasic.builder()
                .groupScheduleId(schedule.getScheduleId())
                .scheduleName(schedule.getScheduleName())
                .scheduleContent(schedule.getScheduleContent())
                .startSchedule(schedule.getStartSchedule().toLocalDate())
                .startTime(schedule.getStartSchedule().toLocalTime())
                .endSchedule(schedule.getEndSchedule().toLocalDate())
                .endTime(schedule.getEndSchedule().toLocalTime())
                .address(schedule.getAddress())
                .subAddress(schedule.getSubAddress())
                .maxMemberCount(schedule.getMaxMemberCount())
                .memberCount(schedule.getMemberSchedules().size())
                .build();
    }
    // 🎯 정기 일정 응답 매핑
    default ScheduleDto.ResponseRecurring toRecurringResponse(Schedule schedule) {
        List<ScheduleDto.RecurringDateDto> recurringDates = getRecurringDates(
                schedule.getStartSchedule(),
                schedule.getEndSchedule(),
                schedule.getDaysOfWeek(),
                schedule.getStartSchedule().toLocalTime()
        );

        return ScheduleDto.ResponseRecurring.builder()
                .groupScheduleId(schedule.getScheduleId())
                .scheduleName(schedule.getScheduleName())
                .scheduleContent(schedule.getScheduleContent())
                .startSchedule(schedule.getStartSchedule().toLocalDate())
                .endSchedule(schedule.getEndSchedule().toLocalDate())
                .daysOfWeek(schedule.getDaysOfWeek())
                .recurringDates(recurringDates)
                .address(schedule.getAddress())
                .subAddress(schedule.getSubAddress())
                .maxMemberCount(schedule.getMaxMemberCount())
                .memberCount(schedule.getMemberSchedules().size())
                .build();
    }

    // ✨ 반복 요일 기반 날짜 계산
    default List<ScheduleDto.RecurringDateDto> getRecurringDates(LocalDateTime start, LocalDateTime end, List<DayOfWeek> daysOfWeek, LocalTime time) {
        List<ScheduleDto.RecurringDateDto> result = new ArrayList<>();
        LocalDate current = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        while (!current.isAfter(endDate)) {
            if (daysOfWeek.contains(current.getDayOfWeek())) {
                result.add(ScheduleDto.RecurringDateDto.builder()
                        .date(current)
                        .time(time)
                        .build());
            }
            current = current.plusDays(1);
        }
        return result;
    }

//    List<ScheduleDto.Response> schedulesToScheduleResponses(List<Schedule> schedules);

    default List<ScheduleDto.CalendarResponse> getCalendarResponse(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> ScheduleDto.CalendarResponse.builder()
                        .startSchedule(schedule.getStartSchedule().toLocalDate())
                        .endSchedule(schedule.getEndSchedule().toLocalDate())
                        .scheduleStatus(schedule.getScheduleStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
