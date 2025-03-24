package com.springboot.schedule.controller;

import com.springboot.member.entity.Member;
import com.springboot.schedule.dto.ScheduleDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

// 틀 구현
@RestController
@RequestMapping("/groups")
@Validated
public class ScheduleController {
    @PostMapping("/{group-id}/schedules")
    public ResponseEntity postSchedule(@RequestBody ScheduleDto.Post schedulePostDto,
                                       @AuthenticationPrincipal Member authenticatedmember,
                                       @PathVariable("group-id") @Positive long groupId) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{group-id}/schedules/{schedule-id}")
    public ResponseEntity patchSchedule(@RequestBody ScheduleDto.Patch schedulePatchDto,
                                       @AuthenticationPrincipal Member authenticatedmember,
                                       @PathVariable("group-id") @Positive long groupId,
                                       @PathVariable("schedule-id") @Positive long scheduleId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{group-id}/schedules/{schedule-id}")
    public ResponseEntity getSchedule(@RequestBody ScheduleDto.Response scheduleResponseDto,
                                      @AuthenticationPrincipal Member authenticatedmember,
                                      @PathVariable("group-id") @Positive long groupId,
                                      @PathVariable("schedule-id") @Positive long scheduleId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{group-id}/schedules")
    public ResponseEntity getSchedules(@RequestParam @Positive int page,
                                       @RequestParam @Positive int size,
                                       @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{group-id}/schedules/{schedule-id}")
    public ResponseEntity deleteSchedule(@AuthenticationPrincipal Member authenticatedmember,
                                          @PathVariable("group-id") @Positive long groupId,
                                          @PathVariable("schedule-id") @Positive long scheduleId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
