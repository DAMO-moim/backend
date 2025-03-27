package com.springboot.member.entity;

import com.springboot.audit.BaseEntity;
import com.springboot.board.entity.Board;
import com.springboot.comment.entity.Comment;
import com.springboot.group.entity.GroupMember;
import com.springboot.message.entity.Message;
import com.springboot.schedule.entity.Schedule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//임시 멤버 엔티티
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String image;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 13, unique = true)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String birth;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Gender gender;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCategory> memberCategories = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<MemberSchedule> memberSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
    private List<Message> messages = new ArrayList<>();

    public void setGroupMember(GroupMember groupMember) {
        groupMembers.add(groupMember);
        if (groupMember.getMember() != this) {
            groupMember.setMember(this);
        }
    }

    public void setMemberCategory(MemberCategory memberCategory) {
        memberCategories.add(memberCategory);
        if (memberCategory.getMember() != this) {
            memberCategory.setMember(this);
        }
    }

    public void setComment(Comment comment) {
        comments.add(comment);
        if (comment.getMember() != this) {
            comment.setMember(this);
        }
    }

    public void setBoard(Board board) {
        boards.add(board);
        if (board.getMember() != this) {
            board.setMember(this);
        }
    }

    public void setMessage(Message message) {
        messages.add(message);
        if (message.getMember() != this) {
            message.setMember(this);
        }
    }

    public void setMemberSchedule(MemberSchedule memberSchedule) {
        memberSchedules.add(memberSchedule);
        if (memberSchedule.getMember() != this) {
            memberSchedule.setMember(this);
        }
    }

    public enum MemberStatus {
        MEMBER_ACTIVE("활동 상태"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public enum Gender {
        MALE("남자"),
        FEMALE("여자");

        @Getter
        private String gender;

        Gender(String gender) {
            this.gender = gender;
        }
    }
}
