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
@Table(name = "club")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private int maxMemberCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupGender gender;

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

    private GroupStatus groupStatus = GroupStatus.GROUP_ACTIVE;

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

    public enum GroupGender {
        MAN("남자"),
        GIRL("여자"),
        ALL("무관");

        @Getter
        private String status;

        GroupGender(String status) {
            this.status = status;
        }
    }

    public enum GroupStatus {
        GROUP_ACTIVE("모임 등록 완료"),
        GROUP_DELETE("모임 삭제 상태");

        @Getter
        private String status;

        GroupStatus(String status) {
            this.status = status;
        }
    }
}
