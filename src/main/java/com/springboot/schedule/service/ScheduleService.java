package com.springboot.schedule.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.service.GroupService;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupService groupService) {
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule, long memberId, long groupId) {
        // ✅ (1) 모임 검증
        Group group = groupService.findVerifiedGroup(groupId); // 이미 작성한 검증 메서드 활용

        // ✅ (2) 모임장 여부 확인
        groupService.validateGroupLeader(group, memberId);

        // 시작 시간이 현재보다 이전인지 검증
        validateNotPastStartTime(schedule.getStartSchedule());

        // 시작 시간이 종료 시간보다 이후인지 검증
        validateStartBeforeEnd(schedule.getStartSchedule(), schedule.getEndSchedule());

        // 일정 최대 인원 수가 1 이상이고, 모임 최대 인원 이하인지 검증
        validateScheduleCapacity(schedule.getMaxMemberCount(), group.getMaxMemberCount());

        // 정기 일정일 경우, 최소 7일 이상의 기간인지 검증
        validateRecurringScheduleLength(schedule);

        // ✅ (3) 일정 상태별 처리
        if (schedule.getScheduleStatus() == Schedule.ScheduleStatus.RECURRING) {
            validateRecurringScheduleLength(schedule);

            if (schedule.getDaysOfWeek() == null || schedule.getDaysOfWeek().isEmpty()) {
                throw new BusinessLogicException(ExceptionCode.INVALID_SCHEDULE_DAYOFWEEK);
            }
        }
        // ✅ (4) 모임 연결
        schedule.setGroup(group);

        // ✅ (5) 저장
        return scheduleRepository.save(schedule);
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

    // 시작 시간이 현재보다 이전인지 검증
    public void validateNotPastStartTime(LocalDateTime startTime) {
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_START_TIME_PAST);
        }
    }

    // 시작 시간이 종료 시간보다 이후인지 검증
    public void validateStartBeforeEnd(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_START_AFTER_END);
        }
    }

    // 정기 일정일 경우, 최소 7일 이상의 기간인지 검증
    public void validateRecurringScheduleLength(Schedule schedule) {
        if (schedule.getScheduleStatus() == Schedule.ScheduleStatus.RECURRING) {
            long days = Duration.between(schedule.getStartSchedule(), schedule.getEndSchedule()).toDays();
            if (days < 7) {
                throw new BusinessLogicException(ExceptionCode.RECURRING_SCHEDULE_TOO_SHORT);
            }
        }
    }

    // 일정 최대 인원 수가 1 이상이고, 모임 최대 인원 이하인지 검증
    public void validateScheduleCapacity(int scheduleMax, int groupMax) {
        if (scheduleMax < 1 || scheduleMax > groupMax) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SCHEDULE_CAPACITY);
        }
    }

}
