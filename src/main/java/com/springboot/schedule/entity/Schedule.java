package com.springboot.schedule.entity;

import com.springboot.group.entity.Group;
import com.springboot.member.entity.MemberSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false)
    private String scheduleName;

    @Column(nullable = false)
    private String scheduleContent;

    @Column(nullable = false)
    private LocalDateTime startSchedule;

    @Column(nullable = false)
    private LocalDateTime endSchedule;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String subAddress;

    @Column(nullable = false)
    private int maxMemberCount;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "schedule_days_of_week", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MemberSchedule> memberSchedules = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus = ScheduleStatus.SINGLE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleState scheduleState = ScheduleState.SCHEDULE_ACTIVE;

    public enum ScheduleStatus {
        SINGLE("단일 일정"),
        CONTINUOUS("연속 일정"),
        RECURRING("정기 일정");

        @Getter
        private String status;

        ScheduleStatus(String status) {
            this.status = status;
        }
    }

    public enum ScheduleState {
        SCHEDULE_ACTIVE("진행중"),     // 일정이 현재 진행 중
        SCHEDULE_COMPLETED("종료"),    // 종료된 일정
        SCHEDULE_DELETE("일정 삭제");

        @Getter
        private String state;

        ScheduleState(String state) {
            this.state = state;
        }
    }

    // 영속성 전이, 동기화
    public void setGroup(Group group) {
        this.group = group;
        if (group.getSchedules().contains(this)) {
            group.setSchedule(this);
        }
    }

    public void setMemberSchedule(MemberSchedule memberSchedule) {
        memberSchedules.add(memberSchedule);
        if (memberSchedule.getSchedule() != this) {
            memberSchedule.setSchedule(this);
        }
    }
}
