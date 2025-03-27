package com.springboot.schedule.service;

import com.springboot.category.entity.Category;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.group.entity.Group;
import com.springboot.group.service.GroupService;
import com.springboot.member.entity.Member;
import com.springboot.member.entity.MemberSchedule;
import com.springboot.member.service.MemberService;
import com.springboot.schedule.dto.CalendarScheduleDto;
import com.springboot.schedule.dto.ParticipantInfoDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.mapper.ScheduleMapper;
import com.springboot.schedule.repository.MemberScheduleRepository;
import com.springboot.schedule.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupService groupService;
    private final MemberService memberService;
    private final MemberScheduleRepository memberScheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupService groupService,
                           MemberService memberService, MemberScheduleRepository memberScheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.groupService = groupService;
        this.memberService = memberService;
        this.memberScheduleRepository = memberScheduleRepository;
        this.scheduleMapper = scheduleMapper;
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
        } else {
            schedule.setDaysOfWeek(null);
        }
        // ✅ (4) 모임 연결
        schedule.setGroup(group);

        MemberSchedule memberSchedule = new MemberSchedule();
        memberSchedule.setSchedule(schedule);
        memberSchedule.setMember(member);
        schedule.setMemberSchedule(memberSchedule);

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
        Schedule findSchedule = findVerifiedSchedule(schedule.getScheduleId());

        // 모임 일정이 등록중 상태인지 검증 ( 종료 상태면 수정이 안되어야 한다. )
        if(findSchedule.getScheduleState().equals(Schedule.ScheduleState.SCHEDULE_COMPLETED)){
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }

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
        validateRecurringScheduleLength(schedule);

        Optional.ofNullable(schedule.getAddress())
                .ifPresent(address -> findSchedule.setAddress(address));
        Optional.ofNullable(schedule.getSubAddress())
                .ifPresent(subAddress -> findSchedule.setSubAddress(subAddress));
        Optional.ofNullable(schedule.getMaxMemberCount())
                .ifPresent(memberCount -> findSchedule.setMaxMemberCount(memberCount));

        //참여인원수가 1이상이고, 이 일정의 모임 최대 인원수보다 낮은지 검증
        validateScheduleCapacity(schedule.getMaxMemberCount(), group.getMaxMemberCount());
        validateNotExceedMaxMemberCount(schedule.getMaxMemberCount(), schedule);

        //현재 일정참여인원수보다 최대인원수보다 낮은지 검증
        validateNotExceedMaxMemberCount(findSchedule.getMaxMemberCount(), findSchedule);
        return scheduleRepository.save(findSchedule);
    }

    // 실제로 존재하는 스케줄인지 검증
    public Schedule findVerifiedSchedule(long scheduleId){
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = optionalSchedule.orElseThrow( () ->
                new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        return schedule;
    }

    @Transactional(readOnly = true)
    public Schedule findSchedule(long memberId, long groupId, long scheduleId) {
        Group group = groupService.findVerifiedGroup(groupId);
        groupService.validateGroupMember(group, memberId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getGroup().getGroupId().equals(groupId)) {
            throw new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_IN_GROUP);
        }

        return schedule;
    }

    public void deleteSchedule(long memberId, long groupId, long scheduleId) {
        // 회원 검증
        Member member = memberService.findVerifiedMember(memberId);

        // 모임 검증
        Group group = groupService.findVerifiedGroup(groupId);

        // 모임장 여부 확인
        groupService.validateGroupLeader(group, memberId);

        // 스케줄 존재 여부 확인
        Schedule schedule = findVerifiedSchedule(scheduleId);
        schedule.setScheduleState(Schedule.ScheduleState.SCHEDULE_DELETE);
    }

    @Transactional(readOnly = true)
    public List<ParticipantInfoDto> findScheduleParticipants(long scheduleId, long memberId, String keyword) {
        // (1) 일정 존재 여부 확인
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCHEDULE_NOT_FOUND));

        // (2) 일정이 속한 모임의 멤버인지 확인
        groupService.validateGroupMember(schedule.getGroup(), memberId);

        // (3) 참여자 목록 가져오기
        Stream<MemberSchedule> stream = schedule.getMemberSchedules().stream();

        // (4) 검색어가 있을 경우 필터링
        if (keyword != null && !keyword.trim().isEmpty()) {
            String processedKeyword = keyword.trim().toLowerCase();
            stream = stream.filter(p -> p.getMember().getName().toLowerCase().contains(processedKeyword));
        }

        return stream
                .map(p -> ParticipantInfoDto.builder()
                        .memberId(p.getMember().getMemberId())
                        .name(p.getMember().getName())
                        .image(p.getMember().getImage())
                        .build())
                .collect(Collectors.toList());
    }

    // 모임 일정 참여
    public void joinSchedule(long memberId, long scheduleId){
        // 회원 검증
        Member member = memberService.findVerifiedMember(memberId);

        // 해당 모임 일정이 있는지 검증
        Schedule schedule = findVerifiedSchedule(scheduleId);

        // 모임 검증 ( 해당 모임 일정의 모임이 실존하는지 )
        Group group = groupService.findVerifiedGroup(schedule.getGroup().getGroupId());

        // 해당 모임에 가입된 회원인지 검증(가입되지 않았을 경우 예외처리)
        if (!groupService.verifyGroupMember(member, group)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND_IN_GROUP);
        }

        // 이미 참여한 회원인지 검증
        if(verifyMemberSchedule(member, schedule)){
            throw new BusinessLogicException(ExceptionCode.MEMBER_ALREADY_JOINED_SCHEDULE);
        }

        // 해당 모임 일정에 참여(영속성전이 떄문에 save 필요없음)
        MemberSchedule memberSchedule = new MemberSchedule();
        memberSchedule.setSchedule(schedule);
        memberSchedule.setMember(member);
        schedule.setMemberSchedule(memberSchedule);
    }

    //모임 일정 취소
    public void joinCancelSchedule(long memberId, long scheduleId){
        // 회원 검증
        Member member = memberService.findVerifiedMember(memberId);

        // 해당 모임 일정이 있는지 검증
        Schedule schedule = findVerifiedSchedule(scheduleId);

        // 모임 검증 ( 해당 모임 일정의 모임이 실존하는지 )
        Group group = groupService.findVerifiedGroup(schedule.getGroup().getGroupId());

        // 만약 모임장이라면 취소할 수 없어야 한다. (모임장이면 예외처리)
        if(groupService.isGroupLeader(group, member.getMemberId())){
            throw new BusinessLogicException(ExceptionCode.LEADER_CANNOT_CANCEL_SCHEDULE);
        }

        // 해당 모임에 가입된 회원인지 검증(가입되지 않았을 경우 예외처리)
        if (!groupService.verifyGroupMember(member, group)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND_IN_GROUP);
        }

        Optional<MemberSchedule> optional = memberScheduleRepository.findByMemberAndSchedule(member, schedule);
        if (optional.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_JOINED_SCHEDULE);
        }

        // 위에서 찾은 MemberSchedule 객체를 가져온다.
        MemberSchedule memberSchedule = optional.get();

        // 연관관계 양방향 제거
        schedule.getMemberSchedules().remove(memberSchedule);
        member.getMemberSchedules().remove(memberSchedule);

        // DB에서 삭제
        memberScheduleRepository.delete(memberSchedule);
    }

    @Transactional(readOnly = true)
    public List<CalendarScheduleDto> findSchedulesByDateAndCategory(LocalDate date, Long categoryId, Long memberId) {
        // 1. 날짜를 LocalDateTime으로 변환 (하루 기준)
        LocalDateTime dateTime = date.atStartOfDay();

        // 2. 이 날짜에 걸칠 수 있는 일정 목록을 모두 가져오기
        List<Schedule> schedules = scheduleRepository.findSchedulesByMemberAndCategoryId(memberId, categoryId);

        // 3. 정기 일정인 경우 해당 요일에 포함되는 것만 필터링
        return schedules.stream()
                .filter(schedule -> isScheduleOnDate(schedule, date))
                .map(schedule -> scheduleMapper.toCalendarScheduleDto(schedule, date))
                  .collect(Collectors.toList());
    }


    private boolean isScheduleOnDate(Schedule schedule, LocalDate date) {
        LocalDate start = schedule.getStartSchedule().toLocalDate();
        LocalDate end = schedule.getEndSchedule().toLocalDate();

        switch (schedule.getScheduleStatus()) {
            case SINGLE:
                return start.isEqual(date);

            case CONTINUOUS:
                return !date.isBefore(start) && !date.isAfter(end);

            case RECURRING:
                return !date.isBefore(start)
                        && !date.isAfter(end)
                        && schedule.getDaysOfWeek().contains(date.getDayOfWeek());

            default:
                return false;
        }
    }

    public boolean verifyMemberSchedule(Member member, Schedule schedule){
        return memberScheduleRepository.existsByScheduleAndMember(schedule, member);
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

    //카테고리별 내가 참여할 모임 일정 조회
    public Page<Schedule> getMySchedulesByCategory(int page, int size, Member member) {
        Member findMember = memberService.findVerifiedMember(member.getMemberId());
        //회원의 우선순위가 가장높은 카테고리를 디폴트로 선택하기위해 우선순위가 가장높은 카테고리를 가져온다.
        Category category = memberService.findTopPriorityCategory(findMember);
        Pageable pageable = PageRequest.of(page, size);

        return memberScheduleRepository.findSchedulesByCategoryId(member, category.getCategoryId(), pageable);
    }

    //카테고리별 내가 참여할 모임 일정 조회
    public Page<Schedule> getMySchedulesByCategory(int page, int size, Member member, long categoryId) {
        Member findMember = memberService.findVerifiedMember(member.getMemberId());
        Pageable pageable = PageRequest.of(page, size);

        return memberScheduleRepository.findSchedulesByCategoryId(member, categoryId, pageable);
    }

    // 매일 새벽 3시에 이 메서드를 자동으로 실행함 (cron 표현식: 초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 0/6 * * *")
    @Transactional
    public void updateExpiredSchedules() {
        // 현재 시각을 가져옴 (이 시각을 기준으로 일정 종료 여부 판단)
        LocalDateTime now = LocalDateTime.now();

        // 종료 시간이 현재 시각 이전이고, 아직 종료 상태(SCHEDULE_COMPLETED)로 바뀌지 않은
        // '진행 중(SCHEDULE_ACTIVE)'인 일정들을 모두 조회함
        List<Schedule> expiredSchedules = scheduleRepository
                .findAllByEndScheduleBeforeAndScheduleState(now, Schedule.ScheduleState.SCHEDULE_ACTIVE);

        // 조회된 일정들을 하나씩 순회하면서 상태를 '종료(SCHEDULE_COMPLETED)'로 변경
        for (Schedule schedule : expiredSchedules) {
            schedule.setScheduleState(Schedule.ScheduleState.SCHEDULE_COMPLETED);
        }

        // 로그로 자동 상태 변경된 일정 개수를 출력함 (서버 로그에서 확인 가능)
        log.info("✅ 자동 종료된 일정 개수: {}", expiredSchedules.size());
    }
}
