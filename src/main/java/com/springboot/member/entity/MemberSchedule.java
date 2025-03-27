package com.springboot.member.entity;

import com.springboot.group.entity.Group;
import com.springboot.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MemberSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberScheduleId;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus = ParticipationStatus.PARTICIPATION_YES;

    public enum ParticipationStatus {
        PARTICIPATION_YES("참석 상태"),
        PARTICIPATION_NO("불참 상태");

        @Getter
        private String status;

        ParticipationStatus(String status) {
            this.status = status;
        }
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        if (!schedule.getMemberSchedules().contains(this)) {
            schedule.setMemberSchedule(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getMemberSchedules().contains(this)) {
            member.setMemberSchedule(this);
        }
    }
}
