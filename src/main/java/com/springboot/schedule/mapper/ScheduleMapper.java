package com.springboot.schedule.mapper;

import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    Schedule schedulePostToSchedule(ScheduleDto.Post schedulePost);
    Schedule schedulePatchToSchedule(ScheduleDto.Patch schedulePatch);
    ScheduleDto.Response scheduleToScheduleResponse(Schedule schedule);
    List<ScheduleDto.Response> schedulesToScheduleResponses(List<Schedule> schedules);
}
