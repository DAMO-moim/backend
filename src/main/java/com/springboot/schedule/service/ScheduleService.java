package com.springboot.schedule.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.service.GroupService;
import com.springboot.member.entity.Member;
import com.springboot.member.service.MemberService;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;
    private final MemberService memberService;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupService groupService, MemberService memberService) {
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
        this.memberService = memberService;
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule, long memberId, long groupId) {
        // 실제 존재하는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);

        // ✅ (1) 모임 검증
        Group group = groupService.findVerifiedGroup(groupId); // 이미 작성한 검증 메서드 활용

        // ✅ (2) 모임장 여부 확인
        groupService.validateGroupLeader(group, memberId);

        validateNotPastStartTime(schedule.getStartSchedule());
        validateStartBeforeEnd(schedule.getStartSchedule(), schedule.getEndSchedule());
        validateScheduleCapacity(schedule.getMaxMemberCount(), group.getMaxMemberCount());
        // 일정 최대 인원 수가 1 이상이고, 모임 최대 인원 이하인지 검증
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
        // 실제 존재하는 회원인지 검증
        Member member = memberService.findVerifiedMember(memberId);

        // 모임 검증
        Group group = groupService.findVerifiedGroup(groupId);

        // 모임장 여부 확인
        groupService.validateGroupLeader(group, memberId);

        // 모임 일정 검증
        Schedule findSchedule = findVerifiedMember(schedule.getScheduleId());

        Optional.ofNullable(schedule.getScheduleName())
                .ifPresent(name -> findSchedule.setScheduleName(name));
        Optional.ofNullable(schedule.getScheduleContent())
                .ifPresent(content -> findSchedule.setScheduleContent(content));
        Optional.ofNullable(schedule.getStartSchedule())
                .ifPresent(start -> findSchedule.setStartSchedule(start));

        //시작 시간이 현재보다 이전인지 검증
        validateNotPastStartTime(schedule.getStartSchedule());

        Optional.ofNullable(schedule.getEndSchedule())
                .ifPresent(end -> findSchedule.setEndSchedule(end));

        //시작 시간이 종료시간보다 이후인지검증
        validateStartBeforeEnd(schedule.getStartSchedule(), schedule.getEndSchedule());
        validateRecurringScheduleLength(schedule);

        Optional.ofNullable(schedule.getAddress())
                .ifPresent(address -> findSchedule.setAddress(address));
        Optional.ofNullable(schedule.getSubAddress())
                .ifPresent(subAddress -> findSchedule.setSubAddress(subAddress));
        Optional.ofNullable(schedule.getMaxMemberCount())
                .ifPresent(memberCount -> findSchedule.setMaxMemberCount(memberCount));

        //참여인원수가 1이상이고, 이 일정의 모임 최대 인원수보다 낮은지 검증
        validateScheduleCapacity(schedule.getMaxMemberCount(), group.getMaxMemberCount());

        //현재 일정참여인원수보다 최대인원수보다 낮은지 검증
        validateNotExceedMaxMemberCount(findSchedule.getMaxMemberCount(), findSchedule);
        return scheduleRepository.save(findSchedule);
    }

    // 실제로 존재하는 스케줄인지 검증
    public Schedule findVerifiedMember(long scheduleId){
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = optionalSchedule.orElseThrow( () ->
                new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        return schedule;
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

    // 현재 일정 참여 인원 수가 최대 일정 참여 인원수보다 큰지 검증
    public void validateNotExceedMaxMemberCount(int scheduleMax, Schedule schedule){
        if (scheduleMax < schedule.getMemberSchedules().size()) {
            throw new BusinessLogicException(ExceptionCode.INVALID_SCHEDULE_COUNT);
        }
    }
}
