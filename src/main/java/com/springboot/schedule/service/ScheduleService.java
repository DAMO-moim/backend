package com.springboot.schedule.service;

import com.springboot.group.service.GroupService;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupService groupService) {
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
    }

    public Schedule createSchedule(Schedule schedule, long groupId, long memberId) {
        return null;
    }

    public Schedule updateSchedule(Schedule schedule, long groupId, long memberId) {
        return null;
    }

    public Schedule findSchedule(long memberId, long groupId, long scheduleId) {
        return null;
    }

    public Page<Schedule> findSchedules(int page, int size, long memberId) {
        return null;
    }

    public Schedule deleteSchedule(long memberId, long groupId, long scheduleId) {
        return null;
    }
}
