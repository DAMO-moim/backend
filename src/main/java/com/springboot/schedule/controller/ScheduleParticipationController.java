package com.springboot.schedule.controller;

import com.springboot.member.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
@Validated
public class ScheduleParticipationController {
    @PostMapping("/{schedule-id}/participation")
    public ResponseEntity postParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{schedule-id}/participation")
    public ResponseEntity getParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{schedule-id}/participation")
    public ResponseEntity deleteParticipationSchedule(@PathVariable("schedule-id") long scheduleId,
                                                    @AuthenticationPrincipal Member authenticatedmember) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
