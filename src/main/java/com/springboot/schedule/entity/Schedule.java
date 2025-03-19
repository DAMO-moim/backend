package com.springboot.schedule.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.group.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
