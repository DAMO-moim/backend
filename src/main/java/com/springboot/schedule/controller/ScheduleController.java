package com.springboot.schedule.controller;

import com.springboot.dto.MultiResponseDto;
import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.schedule.dto.ScheduleDto;
import com.springboot.schedule.dto.ScheduleResponse;
import com.springboot.schedule.entity.Schedule;
import com.springboot.schedule.mapper.ScheduleMapper;
import com.springboot.schedule.service.ScheduleService;
import com.springboot.utils.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@Tag(name = "모임 일정 컨트롤러", description = "모임 일정 관련 컨트롤러")
@RestController
@RequestMapping("/groups/{group-id}")
@Validated
public class ScheduleController {
    private final static String SCHEDULE_DEFAULT_URL = "/groups";
    private final ScheduleMapper scheduleMapper;
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleMapper scheduleMapper, ScheduleService scheduleService) {
        this.scheduleMapper = scheduleMapper;
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "모임 일정 생성", description = "모임 일정을 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 일정 생성 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "400", description = "요청이 잘못 되었음")
    })
    @PostMapping("/schedules")
    public ResponseEntity postSchedule(@RequestBody ScheduleDto.Post schedulePostDto,
                                       @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember,
                                       @PathVariable("group-id") @Positive long groupId) {
        Schedule schedule = scheduleMapper.schedulePostToSchedule(schedulePostDto);

        Schedule createSchedule = scheduleService.createSchedule(schedule, authenticatedmember.getMemberId(), groupId);

        URI location = UriCreator.createUri(SCHEDULE_DEFAULT_URL, createSchedule.getScheduleId());

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "모임 일정 수정", description = "모임 일정을 수정합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 일정 수정 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "400", description = "요청이 잘못 되었음")
    })

    @PatchMapping("/schedules/{schedule-id}")
    public ResponseEntity patchSchedule(@RequestBody ScheduleDto.Patch schedulePatchDto,
                                        @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember,
                                       @PathVariable("group-id") @Positive long groupId,
                                       @PathVariable("schedule-id") @Positive long scheduleId) {
        schedulePatchDto.setScheduleId(scheduleId);

        Schedule schedule = scheduleMapper.schedulePatchToSchedule(schedulePatchDto);

        scheduleService.updateSchedule(schedule, groupId, authenticatedmember.getMemberId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "모임 일정 단일 조회", description = "하나의 모임 일정을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모임 일정 생성 성공"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임 일정을 찾을 수 없습니다.")
    })

    @GetMapping("/schedules/{schedule-id}")
    public ResponseEntity<SingleResponseDto<ScheduleResponse>> getSchedule(
            @Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedMember,
            @PathVariable("group-id") @Positive long groupId,
            @PathVariable("schedule-id") @Positive long scheduleId) {

        Schedule schedule = scheduleService.findSchedule(authenticatedMember.getMemberId(), groupId, scheduleId);

        ScheduleResponse response;
        if (schedule.getScheduleStatus() == Schedule.ScheduleStatus.RECURRING) {
            response = scheduleMapper.toRecurringResponse(schedule);
        } else {
            response = scheduleMapper.toBasicResponse(schedule);
        }

        return ResponseEntity.ok(new SingleResponseDto<>(response));
    }

    @Operation(summary = "모임 일정 삭제", description = "모임 일정을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "모임 일정 삭제 완료"),
            @ApiResponse(responseCode = "401", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "모임 일정이 존재하지 않음")
    })
    @DeleteMapping("/schedules/{schedule-id}")
    public ResponseEntity deleteSchedule(@Parameter(hidden = true) @AuthenticationPrincipal Member authenticatedmember,
                                          @PathVariable("group-id") @Positive long groupId,
                                          @PathVariable("schedule-id") @Positive long scheduleId) {

        scheduleService.deleteSchedule(authenticatedmember.getMemberId(), groupId, scheduleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
