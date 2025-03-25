package com.springboot.schedule.controller;

import com.springboot.member.entity.Member;
import com.springboot.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/schedules")
@Validated
public class ScheduleParticipationController {
    private final ScheduleService scheduleService;

    public ScheduleParticipationController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
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

    @GetMapping("/{schedule-id}/participation")
    public ResponseEntity getParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                   @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{schedule-id}/participation")
    public ResponseEntity deleteParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                      @Parameter(hidden = true) @AuthenticationPrincipal Member member) {
        scheduleService.joinCancelSchedule(member.getMemberId(), scheduleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
