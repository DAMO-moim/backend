package com.springboot.group.entity;

import com.springboot.board.entity.Board;
import com.springboot.category.entity.SubCategory;
import com.springboot.member.entity.Member;
import com.springboot.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private int maxMemberCount;

    @Column(nullable = false)
    private Member.Gender gender;

    @Column(nullable = false)
    private String startBirth;

    @Column(nullable = false)
    private String endBirth;

    @OneToMany(mappedBy = "Group")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "Group")
    private List<Board> boards;

    @ManyToOne
    @JoinColumn(name = "subCategory_id")
    private SubCategory subCategory;
}
