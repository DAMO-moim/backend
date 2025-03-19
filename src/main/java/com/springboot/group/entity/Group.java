package com.springboot.group.entity;

import com.springboot.board.entity.Board;
import com.springboot.category.entity.SubCategory;
import com.springboot.comment.entity.Comment;
import com.springboot.member.entity.Member;
import com.springboot.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    private List<Board> boards;

    @ManyToOne
    @JoinColumn(name = "subCategory_id")
    private SubCategory subCategory;

    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    private List<GroupTag> groupTags = new ArrayList<>();

    // 영속성 전이, 동기화
    public void setBoard(Board board) {
        boards.add(board);
        if (board.getGroup() != this) {
            board.setGroup(this);
        }
    }

    public void setGroupMember(GroupMember groupMember) {
        groupMembers.add(groupMember);
        if (groupMember.getGroup() != this) {
            groupMember.setGroup(this);
        }
    }

    public void setSchedule(Schedule schedule) {
        schedules.add(schedule);
        if (schedule.getGroup() != this) {
            schedule.setGroup(this);
        }
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
        if (!subCategory.getGroups().contains(this)) {
            subCategory.setGroup(this);
        }
    }

    public void setGroupTag(GroupTag groupTag) {
        groupTags.add(groupTag);
        if (groupTag.getGroup() != this) {
            groupTag.setGroup(this);
        }
    }

}
