package com.springboot.schedule.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.group.entity.Group;
import com.springboot.member.entity.MemberSchedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private String scheduleAddress;

    @Column(nullable = false)
    private String scheduleSubAddress;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany
    private List<MemberSchedule> memberSchedules;

    private ScheduleStatus scheduleStatus = ScheduleStatus.SCHEDULE_YES;

    public enum ScheduleStatus {
        SCHEDULE_YES("참여 가능 상태"),
        SCHEDULE_NO("참여 불가 상태");

        @Getter
        private String status;

        ScheduleStatus(String status) {
            this.status = status;
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
