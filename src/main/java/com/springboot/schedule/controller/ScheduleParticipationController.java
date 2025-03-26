package com.springboot.schedule.controller;

import com.springboot.dto.SingleResponseDto;
import com.springboot.member.entity.Member;
import com.springboot.schedule.dto.ParticipantInfoDto;
import com.springboot.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@Validated
public class ScheduleParticipationController {
    private final ScheduleService scheduleService;

    public ScheduleParticipationController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/{schedule-id}/participation")
    public ResponseEntity postParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{schedule-id}/participation")
    public ResponseEntity getParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                   @AuthenticationPrincipal Member authenticatedMember,
                                                   @RequestParam(value= "keyword", required = false) String keyword) {
        List<ParticipantInfoDto> response = scheduleService.findScheduleParticipants(scheduleId, authenticatedMember.getMemberId(), keyword);
        return ResponseEntity.ok(new SingleResponseDto<>(response));
    }

    @DeleteMapping("/{schedule-id}/participation")
    public ResponseEntity deleteParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
