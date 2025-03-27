package com.springboot.schedule.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.group.entity.Group;
import com.springboot.member.entity.Member;
import com.springboot.schedule.dto.CalendarScheduleDto;
import com.springboot.schedule.dto.ParticipantInfoDto;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.mapper.ScheduleMapper;
import com.springboot.schedule.service.ScheduleService;
import com.springboot.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Positive;

@Tag(name = "모임 일정 참여 컨트롤러", description = "모임 일정 참여 관련 컨트롤러")
@RestController
@RequestMapping("/schedules")
@Validated
public class ScheduleParticipationController {
    private final ScheduleService scheduleService;
    private final ScheduleMapper mapper;

    public ScheduleParticipationController(ScheduleService scheduleService, ScheduleMapper mapper) {
        this.scheduleService = scheduleService;
        this.mapper = mapper;
    }

    @Operation(summary = "모임 일정 참여", description = "하나의 모임 일정에 참여합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 참여 성공"),
            @ApiResponse(responseCode = "400", description = "이미 참여하고 있는 회원입니다.")
    })
  
    @PostMapping("/{schedule-id}/participation")
    public ResponseEntity postParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        scheduleService.joinSchedule(member.getMemberId(), scheduleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "일정 참여자 목록 조회", description = "지정된 일정에 참여한 회원들의 ID, 이름, 프로필 이미지 목록을 조회합니다. " +
            "검색어(keyword)를 통해 참여자 이름 필터링도 가능합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "일정 참여자 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "일정을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "403", description = "해당 일정이 속한 모임에 가입된 회원만 조회 가능합니다")
    })
    @GetMapping("/{schedule-id}/participation")
    public ResponseEntity getParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                   @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedMember,
                                                   @RequestParam(value= "keyword", required = false) String keyword) {
        List<ParticipantInfoDto> response = scheduleService.findScheduleParticipants(scheduleId, authenticatedMember.getMemberId(), keyword);
        return ResponseEntity.ok(new SingleResponseDto<>(response));
    }

    @Operation(summary = "모임 일정 취소", description = "참여했던 모임 일정을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 일정 참여 취소 성공"),
            @ApiResponse(responseCode = "404", description = "가입되지 않은 회원입니다."),
            @ApiResponse(responseCode = "400", description = "참여중이 아닌 회원입니다.")
    })
    @DeleteMapping("/{schedule-id}/participation")
    public ResponseEntity deleteParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                      @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        scheduleService.joinCancelSchedule(member.getMemberId(), scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "특정 날짜의 일정 목록 조회 (달력용)", description = "선택한 날짜와 카테고리 기준으로, 해당 날짜에 참여한 일정들의 모임 이름, 일정 제목, 시간, 장소 등의 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "해당 날짜의 일정 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "요청 파라미터 형식이 잘못되었거나, 유효하지 않은 카테고리입니다"),
            @ApiResponse(responseCode = "404", description = "해당 날짜에 표시할 일정이 존재하지 않음")
    })
    @PostMapping("/calendar")
    public ResponseEntity getSchedulesOnDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("categoryId") Long categoryId,
            @Parameter(hidden = true) @AuthenticationPrincipal Member member) {

        List<CalendarScheduleDto> schedules = scheduleService
                .findSchedulesByDateAndCategory(date, categoryId, member.getMemberId());
        return ResponseEntity.ok(new SingleResponseDto<>(schedules));
    }

    @Operation(summary = "카테고리별 모임 일정", description = "카테고리별 모임 일정을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "카테고리별 모임 일정 조회성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임 일정이 존재하지 않음")
    })
    @GetMapping
    public ResponseEntity getGroupsDefault(@RequestParam @Positive int page,
                                           @RequestParam @Positive int size,
                                           @RequestParam(required = false) Long categoryId,
                                           @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember) {
        //만약 categoryName을 입력하지 않았다면 우선순위가 가장 높은 카테고리의 모임 리스트를 조회한다.
        Page<Schedule> schedulePage;
        if(categoryId == null){
            schedulePage = scheduleService.getMySchedulesByCategory(page - 1, size, authenticatedmember);
        }else{
            schedulePage = scheduleService.getMySchedulesByCategory(page - 1, size, authenticatedmember, categoryId);
        }
        List<Schedule> schedules = schedulePage.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>
                (mapper.getCalendarResponse(schedules), schedulePage),
                HttpStatus.OK);

    }
}
